# CLAUDE.md

IBM Bob Labs — a playbook site for replacing repetitive work with Bob.
See `README.md` for the project overview and content spec, `NOTE.md` for the original planning note.

## Current state

No implementation code yet. Phase 1 (skeleton) has not started, and the **site implementation approach is undecided**.
Do not introduce a framework or build tooling on your own — ask first.

## Working rules

- **Prompt bodies are written in English** — participants paste them verbatim. Surrounding explanation follows the site's language.
- `NOTE.md` and `docs/` are gitignored. Never include them in a commit.
- When building UI, follow `docs/DESIGN.md` (IBM Carbon).
  Essentials: 0px corners · IBM Plex Sans · weight 300 for display sizes · 1px hairlines instead of shadows · IBM Blue (#0f62fe) as the only accent.

## Adding content

To add one Lab / Recipe:

1. Write in one line **which repetitive task this replaces**. If you can't, don't build it.
2. Fill in the spec — `Problem → Prompt → Expected Output → Tips → Variations`. **Never skip a section.**
3. Attach all five metadata fields — difficulty / duration / stack / work replaced / expected saving.
4. Actually run the prompt through Bob and write Expected Output from **what you observed**, not from what you assume.
5. One asset = one commit (or one PR). Don't mix several assets into one commit.

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
