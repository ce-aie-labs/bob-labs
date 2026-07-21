#!/usr/bin/env python3
"""Validate that every changed _labs/**/*.md file has the full content spec."""

import re
import subprocess
import sys

REQUIRED_FRONT_MATTER = [
    "title",
    "difficulty",
    "duration",
    "stack",
    "work_replaced",
    "expected_saving",
]

REQUIRED_SECTIONS = [
    "Problem",
    "Prompt",
    "Expected Output",
    "Tips",
    "Variations",
]

FRONT_MATTER_RE = re.compile(r"\A---\n(.*?)\n---\n(.*)\Z", re.DOTALL)


def changed_lab_files(base_ref):
    diff = subprocess.run(
        ["git", "diff", "--name-only", "--diff-filter=ACM", f"{base_ref}...HEAD"],
        capture_output=True, text=True, check=True,
    ).stdout.splitlines()
    return [f for f in diff if f.startswith("_labs/") and f.endswith(".md")]


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


def validate_file(path):
    errors = []
    with open(path, encoding="utf-8") as f:
        text = f.read()

    fields, body = parse_front_matter(text)
    if fields is None:
        return [f"{path}: missing YAML front matter block (---...---)"]

    for field in REQUIRED_FRONT_MATTER:
        if not fields.get(field):
            errors.append(f"{path}: front matter field '{field}' is missing or empty")

    for section in REQUIRED_SECTIONS:
        heading_re = re.compile(
            rf"^##\s+{re.escape(section)}\s*$\n+(.+?)(?=^##\s|\Z)",
            re.MULTILINE | re.DOTALL,
        )
        match = heading_re.search(body)
        if not match or not match.group(1).strip():
            errors.append(f"{path}: section '## {section}' is missing or empty")

    return errors


def main():
    base_ref = sys.argv[1] if len(sys.argv) > 1 else "origin/main"
    files = changed_lab_files(base_ref)
    if not files:
        print("No _labs/**/*.md files changed - nothing to validate.")
        return 0

    all_errors = []
    for path in files:
        all_errors.extend(validate_file(path))

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
