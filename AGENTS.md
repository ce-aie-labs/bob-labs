# AGENTS.md

Instructions for coding agents working in this repository.

IBM Bob Labs - a playbook site for replacing repetitive work with Bob.
See `README.md` for the project overview and content spec, `NOTE.md` for the original planning note.

## Current state

Phase 1 (skeleton) has started. Site is Jekyll, hosted on GitHub Pages, built and deployed via GitHub Actions (`.github/workflows/pages.yml`).
Do not introduce a different framework or build tooling on your own - ask first.

## Working rules

- **Every lab is bilingual, mandatory** - an English file and a Korean file, added together. Prompt language matches the file, not a fixed rule: the English file's Prompt is English, the Korean file's Prompt is Korean, written the way an actual Korean-speaking participant would type it - not a stiff translation. Korean customers paste Bob prompts in Korean, so the Korean Prompt needs its own verification pass against Bob in Korean, not just a translation of the English Expected Output.
- `NOTE.md` and `docs/` are gitignored. Never include them in a commit, and don't add gitignore exceptions to sneak part of `docs/` in. `docs/bob/` is a full Markdown mirror of IBM's product documentation from `bob.ibm.com/docs`; this repository is public, so mirroring it here would republish someone else's documentation and go stale the moment they update it. Link to the live docs instead.
- When building UI, follow `DESIGN.md` (IBM Carbon). It is tool-generated - don't hand-edit it.
  Essentials: 0px corners · IBM Plex Sans · weight 300 for display sizes · 1px hairlines instead of shadows · IBM Blue (#0f62fe) as the only accent.

## Adding content

To add one Lab / Recipe, you're always producing **two files**, one asset:

1. Write in one line **which repetitive task this replaces**. If you can't, don't build it.
2. Place the English file at `_labs/<stack>/<slug>.md` and the Korean file at `_labs_ko/<stack>/<slug>.md` - same relative path, mirrored, one file per asset per language. These are Jekyll collections, so the underscore prefix is required on both - don't rename either to `content/`.
   The branch name keeps the hyphenated form (`content/<stack>-<slug>`) as a label only - it doesn't need to match the physical folders, e.g. branch `content/spring-boot-explain-repo` holds both `_labs/spring-boot/explain-repo.md` and `_labs_ko/spring-boot/explain-repo.md`.
3. In each file's front matter, set `lang: en` or `lang: ko` - this is what templates and CI key off, not the folder name.
   Also set `category:` to one of the values in `_data/categories.yml`. Unlike `difficulty`, the category value stays **English in both language files** - it is a machine key that the homepage filter and CI validate against, and the Korean label is looked up for display. A category that is not in that file fails CI.
4. Add a `title:` front matter field in each file's own language - the page heading Jekyll renders.
5. Fill in the spec, headings in the file's own language:
   - English: `Problem → Prompt → Expected Output → Tips → Variations`
   - Korean: `문제 → 프롬프트 → 기대 결과 → 팁 → 응용`
   **Never skip a section, in either language.**
6. Attach all eight front matter fields in each file - `title`, `lang`, `category`, `difficulty`, `duration`, `stack`, `work_replaced`, `expected_saving`. A CI check (`validate-content`) blocks the PR if any field or section is missing in either file, or if one language's file exists without its sibling.
7. Actually run each language's prompt through Bob and write its Expected Output from **what you observed in that language**, not a translation of the other file's output.
   If you can't run Bob yet (drafting from [bob.ibm.com/docs](https://bob.ibm.com/docs) or the local `docs/bob/` mirror instead), that's fine as a starting draft - but say so explicitly with a `<!-- Bob-verify: ... -->` comment on the Expected Output, and get a real Bob pass before the asset is used with actual participants.
8. One asset = one commit (or one PR) = **both language files together**. Don't mix several assets into one commit.

Three reference pairs, three different shapes:

- `_labs/spring-boot/explain-repo.md` + `_labs_ko/…` - a stack-tied lab (`stack: Java, Spring Boot`), single prompt, Expected Output verified by an actual dry run.
- `_labs/bob-features/generate-architecture-diagram.md` + `_labs_ko/…` - a **Bob Features** lab (`stack: Any`), single prompt - demos a capability of Bob itself rather than a language/framework.
- `_labs/bob-features/plan-then-build.md` + `_labs_ko/…` - a **multi-step** lab: four sequential prompts as `### Step N` subheadings inside the single `## Prompt` section, each with its own checkpoint. The 5-section spec is unchanged - `###` subheadings don't affect it, so multi-step labs need no special handling.

### Challenge labs

`difficulty: Challenge` is a value, not a separate content type or site section - same five sections, same validator. What changes is the `## Prompt` section: state the goal, then put the help behind `<details>` so it is there without being handed over.

```markdown
## Prompt

Get Bob to <goal>, without being told the prompt.

<details markdown="1">
<summary>Stuck? A prompt skeleton</summary>

    <the shape, with blanks the participant fills in>

</details>

<details markdown="1">
<summary>The full prompt</summary>

    <a prompt known to work>

</details>
```

`markdown="1"` is required. Without it Kramdown leaves the fenced block unprocessed, so no `<pre>` is produced and the prompt gets no copy button - which defeats the point.

`difficulty: Challenge` is a lab attribute and has nothing to do with the **Bobathon** (`/bobathon/`), which is the event page - process, rubric and submission. Earlier drafts used "Challenge Labs" for both; they are separate things.

Do not leave `## Prompt` empty to make it a challenge - CI rejects an empty section, and an empty section helps nobody. Challenge labs are the bridge from Guided to Bring Your Own Work, so they matter most for the least experienced participant in the room.

### Write stack-agnostic first

Use `stack: Any` whenever the lab isn't genuinely tied to one language or framework, and put per-stack differences in the asset's own **Variations** section. "Explain this repo" or "review this diff" is the same lab in Java and Python - writing it five times per stack is exactly the duplication the reusability principle exists to prevent. Write a stack-specific lab only when the stack really changes the prompt (a Spring Boot 2→3 migration does; "explain this repo" doesn't).

Bob-capability demos - subagents, skills, MCP, Plan mode, diagram/report generation - go in `bob-features/`, not under a stack name.

## Running the site

`script/preview` - it checks the Ruby version, installs dependencies on first run, and prints the URLs. The site lives at <http://127.0.0.1:4000/bob-labs/>; the bare root 404s. Ruby 3.0+ is required and macOS ships 2.6, which the script explains rather than failing on a gem. There is a dev container for running it without a local Ruby. See CONTRIBUTING.md.

## Git workflow

Remote is `origin` → `github.com/ce-aie-labs/bob-labs`. Default branch is `main`.

1. Branch off `main` - never commit directly to it.
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
4. Could this be one `stack: Any` lab instead of one per stack? If yes, write it that way.
5. Is it P0? **While P0 items remain, don't touch P1/P2.**

If any of the first three is "no", don't build it. Reusability beats asset count.

## Don't

- Build a curriculum that must be worked through in order - this is a cookbook, not a course.
- Ship a prompt that requires reading an explanation to work. One copy button should be enough.
- Write one-off examples that only apply in their own context.
- Make decisions that depend on the open items at the bottom of `README.md`.
- Build Java Modernization assets - it's a paid Bob premium package we don't have access to yet (see README's "Open decisions").
