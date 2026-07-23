# Contributing to IBM Bob Labs

Thanks for helping build this out. This document is the detailed version of the rules in `README.md`'s "Add a lab" and "Content unit spec" sections and `AGENTS.md`.

## Start here (day 1)

Before you build anything, spend your first hour proving the whole loop works end to end for you. Every lab you write afterwards is this same loop at volume.

1. **Get Bob running and signed in.** If your access isn't sorted, raise it immediately - it is the one thing that blocks everything and cannot be worked around. Don't start drafting around it.
2. **Run the reference lab yourself.** Open `_labs_en/spring-boot/explain-repo.md`, clone [spring-projects/spring-petclinic](https://github.com/spring-projects/spring-petclinic), and run its Prompt through Bob against that repo. Compare what you get to the lab's Expected Output. This is exactly the loop you repeat for every lab - feel it once before writing your own.
3. **Skim the three reference shapes** in [Reference assets](#reference-assets) so you know which one to copy.
4. **Ship your first lab in week 1.** Pick the *smallest* lab you own, not the most interesting one. The point of week 1 is to get one bilingual pair all the way through CI and review, so any blocker shows up now instead of in week 4.

## Ground rules

- Every change goes through a PR. `main` is never committed to directly - it's enforced by branch protection (one approval + passing status checks required).
- Branch off `main`:
  - `content/<stack>-<slug>` for a Lab/Recipe asset, e.g. `content/spring-boot-explain-repo`. This is a naming label only - it doesn't need to match the physical file path.
  - `feat/`, `fix/`, `docs/` for everything else.
- Commit messages are in English, imperative mood.
- Before committing, run `git status` and confirm `NOTE.md` and `docs/` are not staged - they're local-only working documents.

## Adding a Lab / Recipe asset

One asset is **two files, added together in one commit/PR**:

- `_labs_en/<stack>/<slug>.md` - English
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

### Copy this skeleton

The fastest start: paste each block into its file and fill the blanks. Every field and every section is required - leave none empty or CI rejects the PR. Delete the `<!-- comments -->` as you replace them. Note the differences in the Korean file: headings, `difficulty`, `duration`, `work_replaced` and `expected_saving` are written in Korean, but `lang`, `category` and `stack` values stay exactly as shown.

**`_labs_en/<stack>/<slug>.md`** (English)

````markdown
---
title:            # page heading, in English
lang: en
category:         # exact value from _data/categories.yml, e.g. Code Review
difficulty:       # Guided or Challenge
duration:         # e.g. 5 min
stack:            # e.g. Java, Spring Boot  (or  Any  when not tied to a stack)
work_replaced:    # the repetitive task, one line
expected_saving:  # e.g. 30 min → 5 min
---

## Problem

<!-- Who is stuck on what, and why it keeps happening. 2-3 sentences. -->

## Prompt

<!-- The exact prompt, in a fenced code block so the site gives it a copy button. -->

```
<paste the prompt>
```

## Expected Output

<!-- What Bob actually produced. Name the practice repo you ran it against. Not a guess. -->

## Tips

<!-- What to check, the common failure, how to reprompt. -->

## Variations

<!-- 1-3 ways to adapt it, e.g. per stack. -->
````

**`_labs_ko/<stack>/<slug>.md`** (Korean - same slug and folder as the English file)

````markdown
---
title:            # 페이지 제목, 한국어로
lang: ko
category:         # _data/categories.yml 값 그대로, 영어로 (예: Code Review)
difficulty:       # 가이드 또는 챌린지
duration:         # 예: 5분
stack:            # 예: Java, Spring Boot  (스택과 무관하면  Any)
work_replaced:    # 대체하는 반복 업무, 한 줄
expected_saving:  # 예: 30분 → 5분
---

## 문제

<!-- 누가 무엇에 막혀 있고 왜 반복되는지. 2~3문장. -->

## 프롬프트

<!-- 실제 프롬프트를 코드블록 안에 (그래야 복사 버튼이 붙음). 한국어 참가자가 실제로 칠 법한 문장으로 - 영어 번역이 아니라. -->

```
<프롬프트 붙여넣기>
```

## 기대 결과

<!-- Bob을 한국어로 직접 돌려서 나온 결과. 어떤 연습 repo에 대고 돌렸는지 명시. 영어 결과의 번역이 아님. -->

## 팁

<!-- 확인할 것, 흔한 실패, 다시 프롬프트하는 법. -->

## 응용

<!-- 1~3가지 변형 (예: 스택별). -->
````

For a Challenge lab, the sections are identical - only `## Prompt` changes shape (goal first, help behind `<details>`). See [Challenge labs](#challenge-labs) below.

### Reference assets

Three complete pairs to copy from, covering three different shapes:

| Reference | Shape |
|---|---|
| `_labs_en/spring-boot/explain-repo.md` | Stack-tied (`stack: Java, Spring Boot`), single prompt |
| `_labs_en/bob-features/generate-architecture-diagram.md` | Stack-agnostic (`stack: Any`), single prompt, demos a Bob capability |
| `_labs_en/bob-features/plan-then-build.md` | Multi-step - four sequential prompts as `### Step N` subheadings inside `## Prompt`, each with a checkpoint |

Each has a `_labs_ko/` counterpart at the same path. `README.md`'s "Content unit spec" has the full field/section reference.

### Practice repositories

Run every Prompt against the same shared set, not whatever repo you happen to have open. Consistent inputs are what let a reviewer reproduce your Expected Output, and they keep labs across the library comparable. Match the repo to your lab's stack; for a `stack: Any` lab, use whichever fits your example and note which one in the Expected Output (the reference `explain-repo` does exactly this).

| Stack | Repo |
|---|---|
| Java / Spring Boot | [spring-projects/spring-petclinic](https://github.com/spring-projects/spring-petclinic) |
| Java / legacy monolith | [mybatis/jpetstore-6](https://github.com/mybatis/jpetstore-6) - Spring + MyBatis, deliberately older |
| Python / FastAPI | [grillazz/fastapi-sqlalchemy-asyncpg](https://github.com/grillazz/fastapi-sqlalchemy-asyncpg) |
| Frontend / React | [alan2207/bulletproof-react](https://github.com/alan2207/bulletproof-react) |
| Data & Documents | OpenAPI spec: the [Swagger Petstore](https://github.com/OAI/OpenAPI-Specification/blob/main/examples/v3.0/petstore.yaml). Excel: a sample workbook under `fixtures/`, added alongside the first Data & Documents lab |

If a lab genuinely needs a repo none of these cover, add the repo to this table in the same PR with a one-line reason - don't quietly point at something else, or the next person can't reproduce you.

These are for **authoring** the labs. What participants run against on event day is a separate question - they may not be able to bring their own code for IP reasons - and is still open in `README.md`.

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

That said, seeing your own work is worth something, and some things genuinely are invisible until built: how your title wraps in the card grid, where the auto-truncated preview text cuts off, whether a long metadata value breaks the badge row, and both themes. If you are touching layouts, CSS or anything under `_layouts/`, `_includes/` or `assets/`, a preview is not optional.

### Running the site

```sh
script/preview
```

That is it. The script checks your Ruby version, installs dependencies on first run, and prints the URLs. If something is missing it tells you exactly what to do rather than failing on a gem you have never heard of.

Open <http://127.0.0.1:4000/bob-labs/>, which redirects to `/en/`. **Note the `/bob-labs/`** - dropping it 404s, because the site is built for a project path rather than a domain root. Check both languages (`/bob-labs/en/` and `/bob-labs/ko/`) and both themes, using the toggle in the header.

Jekyll needs **Ruby 3.0 or newer and macOS ships 2.6**, so on a Mac you will likely need `brew install ruby` first - the script will say so if you do.

If you work with a coding agent in this repository, `.claude/skills/preview/` tells it how to run the site and what to look at - so "check this renders" does not turn into it rediscovering the Ruby version trap.

### Or skip installing anything

The repository has a dev container, so you can run the site without Ruby on your machine:

- **In the browser**: on GitHub, `Code` → `Codespaces` → create one. Codespaces has to be enabled for the organization first, so this cannot bill anyone by surprise.
- **Locally**: VS Code with Docker, then `Reopen in Container`.

Then `script/preview` as above. See `.devcontainer/README.md`.

## Other changes (docs, site, tooling)

Same PR flow, branch prefix `feat/`, `fix/`, or `docs/` depending on what changed. `AGENTS.md`'s "Current state" section describes the site stack (Jekyll, GitHub Actions, GitHub Pages) - don't introduce a different framework or build tool without asking first.

## What makes a lab worth keeping

CI checks that a lab is *well-formed*. It cannot check that it is *worth keeping* - that is what review is for, and it is the same bar whether you are writing the lab or reviewing someone else's. A lab merges only when every line below is yes.

- [ ] **Shows a genuinely useful way to use Bob.** The lab produces a real result a developer wants, not a scenario contrived to show off a feature. `work_replaced` names the kind of work it helps with. If you can't say in one line what it's good for, it isn't a lab.
- [ ] **First result in five minutes, by copy-paste.** Someone lands on the page, copies the Prompt, and has something useful before they'd have given up. No setup essay, no "read this first."
- [ ] **A stranger can reuse it.** Nothing depends on your machine, your private repo, or context only you have. Running against a [practice repo](#practice-repositories) is what makes this true - use one.
- [ ] **The Before/After is obvious.** `expected_saving` is concrete and believable (`30 min → 5 min`), and the Expected Output actually shows what "after" looks like.
- [ ] **`stack: Any` unless the stack changes the prompt.** "Explain this repo" is one lab, not five. Reserve a stack-specific lab for when the stack *is* the point, like a Spring Boot 2→3 migration.
- [ ] **The Prompt was actually run through Bob, in its own language.** This is the one thing no tool catches and the whole library's credibility rests on it. Expected Output describes what you saw, not what you assume Bob would do.
- [ ] **P0 before P1.** While P0 labs remain, P1/P2 work waits.

Reusability beats asset count. A lab nobody reopens is worse than no lab - it is noise the next person has to wade through. A rejected draft is cheaper than a kept one that misleads.

## Questions

- Project overview and content spec: `README.md`
- Rules written for coding agents (Bob, Claude, etc.) working in this repo: `AGENTS.md`
