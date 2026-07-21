# Contributing to IBM Bob Labs

Thanks for helping build this out. This document is the detailed version of the rules in `README.md`'s "Contributing" section and `AGENTS.md`.

## Ground rules

- Every change goes through a PR. `main` is never committed to directly - it's enforced by branch protection (one approval + passing status checks required).
- Branch off `main`:
  - `content/<stack>-<slug>` for a Lab/Recipe asset, e.g. `content/spring-boot-explain-repo`. This is a naming label only - it doesn't need to match the physical file path.
  - `feat/`, `fix/`, `docs/` for everything else.
- Commit messages are in English, imperative mood.
- Before committing, run `git status` and confirm `NOTE.md` and `docs/` are not staged - they're local-only working documents.

## Adding a Lab / Recipe asset

One asset is **two files, added together in one commit/PR**:

- `_labs/<stack>/<slug>.md` - English
- `_labs_ko/<stack>/<slug>.md` - Korean

Both are required. Korean customers paste Bob prompts in Korean too, so the Korean file isn't a translation of the English one's Prompt - write it the way an actual Korean-speaking participant would type it.

Each file needs:

1. **Front matter**: `title`, `lang` (`en`/`ko`), `category`, `difficulty`, `duration`, `stack`, `work_replaced`, `expected_saving`. Values are written in the file's own language; the YAML keys stay in English in both files.
   `category` is the one exception - its *value* stays English in both files too, because it is a machine key the homepage filter and CI validate against. Pick one from `_data/categories.yml`; the Korean label is looked up for display.
2. **The full content spec, headings in the file's own language**:
   - English: `Problem → Prompt → Expected Output → Tips → Variations`
   - Korean: `문제 → 프롬프트 → 기대 결과 → 팁 → 응용`
   No section may be blank.
3. **A Prompt actually run through Bob, in that language.** Expected Output describes what you observed, not what you assume Bob would do, and not a translation of the other file's output.

The `validate-content` CI check enforces all of this automatically, including that both language files exist - a PR adding only one language fails.

### Reference assets

Three complete pairs to copy from, covering three different shapes:

| Reference | Shape |
|---|---|
| `_labs/spring-boot/explain-repo.md` | Stack-tied (`stack: Java, Spring Boot`), single prompt |
| `_labs/bob-features/generate-architecture-diagram.md` | Stack-agnostic (`stack: Any`), single prompt, demos a Bob capability |
| `_labs/bob-features/plan-then-build.md` | Multi-step - four sequential prompts as `### Step N` subheadings inside `## Prompt`, each with a checkpoint |

Each has a `_labs_ko/` counterpart at the same path. `README.md`'s "Content unit spec" has the full field/section reference.

### Challenge labs

`difficulty: Challenge` is a value, not a separate content type. Same five sections; the difference is that `## Prompt` states the goal and hides the help behind `<details>` - a skeleton first, a working prompt second. Don't leave `## Prompt` empty to make it harder; CI rejects that, and it helps nobody. See AGENTS.md for the shape.

### Prefer `stack: Any`

Most repetitive work isn't stack-specific - "explain this repo" is the same lab in Java and Python. Write it once with `stack: Any` and put per-stack differences in the **Variations** section, rather than writing five near-duplicates. Reserve stack-specific labs for cases where the stack genuinely changes the prompt (a Spring Boot 2→3 migration does).

## Previewing your work

**For a lab, you almost certainly do not need to.** Opening the `.md` file in a browser will not work - it is Markdown, and Jekyll turns it into HTML at build time. Opening a built file from `_site/` will not work either: the site is served under `/bob-labs/`, so every stylesheet, font and link is an absolute path that resolves to your disk root over `file://` and 404s.

You do not need any of that, because everything a lab PR needs checking for is already covered:

| What needs checking | Who checks it |
|---|---|
| All front matter fields present, `category` valid | `validate-content` CI |
| All five sections present and non-empty | `validate-content` CI |
| Both language files exist | `validate-content` CI |
| Does the prose read well | GitHub renders your Markdown in the PR |
| Does the page look right | It renders exactly like the four labs already published |
| **Does the prompt actually work in Bob** | **You, by running it. Nothing else can.** |

Spend your time on the last row. It is the only one that is genuinely at risk, and the only one no tool will catch.

### Running the site locally

Needed if you are changing layouts, CSS, or anything under `_layouts/`, `_includes/` or `assets/` - and optional if you just want to see your lab in place.

```sh
bundle install
bundle exec jekyll serve
```

Then open <http://127.0.0.1:4000/bob-labs/>. Note the `/bob-labs/` - the root URL alone will 404, because the site is built for a project path.

**Ruby 3.0 or newer is required, and macOS ships 2.6.** With the system Ruby, `bundle install` fails on `ffi ... requires ruby version >= 3.0`. On macOS:

```sh
brew install ruby
export PATH="/opt/homebrew/opt/ruby/bin:$PATH"   # add to your shell profile to keep it
```

Preview both languages - `/bob-labs/` and `/bob-labs/ko/` - and both themes, using the toggle in the header.

## Other changes (docs, site, tooling)

Same PR flow, branch prefix `feat/`, `fix/`, or `docs/` depending on what changed. `AGENTS.md`'s "Current state" section describes the site stack (Jekyll, GitHub Actions, GitHub Pages) - don't introduce a different framework or build tool without asking first.

## What gets merged

A PR is judged by the same checklist used for the content itself:

- Does copy-paste produce a first result within 5 minutes?
- Does it convert to a Before/After in minutes?
- Can someone else pick it up as-is?
- Is it P0? While P0 items remain, P1/P2 waits.

Reusability beats asset count. A rejected lab is cheaper than a lab nobody reopens.

## Questions

- Project overview and content spec: `README.md`
- Rules written for coding agents (Bob, Claude, etc.) working in this repo: `AGENTS.md`
