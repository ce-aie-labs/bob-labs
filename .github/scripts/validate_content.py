#!/usr/bin/env python3
"""Validate that every changed _labs(_ko)/**/*.md file has the full content
spec in its own language, and that its bilingual sibling exists."""

import os
import re
import subprocess
import sys

REQUIRED_FRONT_MATTER = [
    "title",
    "lang",
    "category",
    "difficulty",
    "duration",
    "stack",
    "work_replaced",
    "expected_saving",
]

CATEGORIES_FILE = "_data/categories.yml"
CATEGORY_LINE_RE = re.compile(r'^"(.+?)"\s*:\s*"(.+?)"\s*$')

REQUIRED_SECTIONS = {
    "en": ["Problem", "Prompt", "Expected Output", "Tips", "Variations"],
    "ko": ["문제", "프롬프트", "기대 결과", "팁", "응용"],
}

COLLECTION_ROOTS = {
    "_labs": ("_labs_ko", "en", "ko"),
    "_labs_ko": ("_labs", "ko", "en"),
}

FRONT_MATTER_RE = re.compile(r"\A---\n(.*?)\n---\n(.*)\Z", re.DOTALL)


def changed_lab_files(base_ref):
    diff = subprocess.run(
        ["git", "diff", "--name-only", "--diff-filter=ACM", f"{base_ref}...HEAD"],
        capture_output=True, text=True, check=True,
    ).stdout.splitlines()
    return [
        f for f in diff
        if (f.startswith("_labs/") or f.startswith("_labs_ko/")) and f.endswith(".md")
    ]


def parse_front_matter(text):
    match = FRONT_MATTER_RE.match(text)
    if not match:
        return None, text
    raw_fm, body = match.groups()
    fields = {}
    for line in raw_fm.splitlines():
        if ":" not in line:
            continue
        key, _, value = line.partition(":")
        fields[key.strip()] = value.strip()
    return fields, body


def load_categories():
    """Read the canonical category list from _data/categories.yml.

    Hand-parsed rather than via PyYAML so the check needs no dependency
    beyond a stock Python in CI. The file is a flat "key": "value" map.
    """
    categories = []
    try:
        with open(CATEGORIES_FILE, encoding="utf-8") as f:
            for line in f:
                line = line.strip()
                if not line or line.startswith("#"):
                    continue
                match = CATEGORY_LINE_RE.match(line)
                if match:
                    categories.append(match.group(1))
    except FileNotFoundError:
        return None
    return categories or None


def sibling_path(path):
    for root, (sibling_root, own_lang, sibling_lang) in COLLECTION_ROOTS.items():
        if path.startswith(root + "/"):
            rel = path[len(root) + 1:]
            return f"{sibling_root}/{rel}", own_lang, sibling_lang
    return None, None, None


def validate_file(path, categories=None):
    errors = []
    with open(path, encoding="utf-8") as f:
        text = f.read()

    fields, body = parse_front_matter(text)
    if fields is None:
        return [f"{path}: missing YAML front matter block (---...---)"]

    for field in REQUIRED_FRONT_MATTER:
        if not fields.get(field):
            errors.append(f"{path}: front matter field '{field}' is missing or empty")

    category = fields.get("category")
    if category and categories and category not in categories:
        errors.append(
            f"{path}: category '{category}' is not one of the canonical categories. "
            f"Use one of: {', '.join(categories)}. "
            f"To add a new one, add it to {CATEGORIES_FILE} and the priority "
            f"tables in README.md."
        )

    lang = fields.get("lang")
    sections = REQUIRED_SECTIONS.get(lang)
    if sections is None:
        errors.append(
            f"{path}: front matter field 'lang' must be 'en' or 'ko' (got '{lang}')"
        )
        sections = []

    for section in sections:
        heading_re = re.compile(
            rf"^##\s+{re.escape(section)}\s*$\n+(.+?)(?=^##\s|\Z)",
            re.MULTILINE | re.DOTALL,
        )
        match = heading_re.search(body)
        if not match or not match.group(1).strip():
            errors.append(f"{path}: section '## {section}' is missing or empty")

    sibling, own_lang, sibling_lang = sibling_path(path)
    if sibling and not os.path.exists(sibling):
        errors.append(
            f"{path}: bilingual sibling '{sibling}' does not exist - "
            f"every lab needs both an {own_lang} and a {sibling_lang} version"
        )

    return errors


def main():
    base_ref = sys.argv[1] if len(sys.argv) > 1 else "origin/main"
    files = changed_lab_files(base_ref)
    if not files:
        print("No _labs/**/*.md or _labs_ko/**/*.md files changed - nothing to validate.")
        return 0

    categories = load_categories()
    if categories is None:
        print(f"Warning: could not read {CATEGORIES_FILE} - skipping category validation.")

    all_errors = []
    for path in files:
        all_errors.extend(validate_file(path, categories))

    if all_errors:
        print("Content spec validation failed:\n")
        for err in all_errors:
            print(f"  - {err}")
        print(f"\n{len(files)} file(s) checked, {len(all_errors)} problem(s) found.")
        return 1

    print(f"{len(files)} file(s) checked, content spec is complete.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
