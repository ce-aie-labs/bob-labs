# AGENTS.md

Instructions for coding agents working in this repository.

IBM Bob Labs — a playbook site for replacing repetitive work with Bob.
See `README.md` for the project overview and content spec, `NOTE.md` for the original planning note.

## Current state

Phase 1 (skeleton) has started. Site is Jekyll, hosted on GitHub Pages, built and deployed via GitHub Actions (`.github/workflows/pages.yml`).
Do not introduce a different framework or build tooling on your own — ask first.

## Working rules

- **Every lab is bilingual, mandatory** — an English file and a Korean file, added together. Prompt language matches the file, not a fixed rule: the English file's Prompt is English, the Korean file's Prompt is Korean, written the way an actual Korean-speaking participant would type it — not a stiff translation. Korean customers paste Bob prompts in Korean, so the Korean Prompt needs its own verification pass against Bob in Korean, not just a translation of the English Expected Output.
- `NOTE.md` and `docs/` are gitignored. Never include them in a commit.
- When building UI, follow `docs/DESIGN.md` (IBM Carbon).
  Essentials: 0px corners · IBM Plex Sans · weight 300 for display sizes · 1px hairlines instead of shadows · IBM Blue (#0f62fe) as the only accent.

## Adding content

To add one Lab / Recipe, you're always producing **two files**, one asset:

1. Write in one line **which repetitive task this replaces**. If you can't, don't build it.
2. Place the English file at `_labs/<stack>/<slug>.md` and the Korean file at `_labs_ko/<stack>/<slug>.md` — same relative path, mirrored, one file per asset per language. These are Jekyll collections, so the underscore prefix is required on both — don't rename either to `content/`.
   The branch name keeps the hyphenated form (`content/<stack>-<slug>`) as a label only — it doesn't need to match the physical folders, e.g. branch `content/spring-boot-explain-repo` holds both `_labs/spring-boot/explain-repo.md` and `_labs_ko/spring-boot/explain-repo.md`.
3. In each file's front matter, set `lang: en` or `lang: ko` — this is what templates and CI key off, not the folder name.
4. Add a `title:` front matter field in each file's own language — the page heading Jekyll renders.
5. Fill in the spec, headings in the file's own language:
   - English: `Problem → Prompt → Expected Output → Tips → Variations`
   - Korean: `문제 → 프롬프트 → 기대 결과 → 팁 → 응용`
   **Never skip a section, in either language.**
6. Attach all six front matter fields in each file — `title`, `lang`, `difficulty`, `duration`, `stack`, `work_replaced`, `expected_saving`. A CI check (`validate-content`) blocks the PR if any field or section is missing in either file, or if one language's file exists without its sibling.
7. Actually run each language's prompt through Bob and write its Expected Output from **what you observed in that language**, not a translation of the other file's output.
8. One asset = one commit (or one PR) = **both language files together**. Don't mix several assets into one commit.

See `_labs/spring-boot/explain-repo.md` and `_labs_ko/spring-boot/explain-repo.md` for a complete reference pair.

## Git workflow

Remote is `origin` → `github.com/ce-aie-labs/bob-labs`. Default branch is `main`.

1. Branch off `main` — never commit directly to it.
   `content/<stack>-<slug>` for assets, `feat/`, `fix/`, `docs/` for everything else.
2. One asset (or one logical change) per commit. Commit messages in English, imperative mood.
3. Before committing, run `git status` and confirm `NOTE.md` and `docs/` are not staged.
4. Push and open a PR: `git push -u origin <branch>` then `gh pr create`.
5. Commit and push only when asked. Don't do it as a side effect of finishing a task.

## Decision checklist

When unsure whether a piece of content or a feature belongs, check in this order.

1. Does copy-paste produce a first result within 5 minutes?
2. Does it convert to a Before/After in minutes?
3. Can someone else pick it up as-is?
4. Is it P0? **While P0 items remain, don't touch P1/P2.**

If any of the first three is "no", don't build it. Reusability beats asset count.

## Don't

- Build a curriculum that must be worked through in order — this is a cookbook, not a course.
- Ship a prompt that requires reading an explanation to work. One copy button should be enough.
- Write one-off examples that only apply in their own context.
- Make decisions that depend on the open items at the bottom of `README.md`.
