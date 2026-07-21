---
name: Asset Library lab
about: Build one Lab/Recipe for the Asset Library
title: "[stack] "
labels: asset
---

## Domain / stack

<!-- e.g. Java - Spring Boot -->

## What repetitive task will this replace?

<!-- One line. If you can't answer this, don't build it. -->

## Where the files go

Two files, one asset - `_labs/<stack>/<slug>.md` (English) and `_labs_ko/<stack>/<slug>.md` (Korean), same relative path. Use `<stack>` = `bob-features` for Bob-capability demos, or `stack: Any` in front matter for any lab that isn't genuinely tied to one language/framework. (Branch name still uses the hyphenated `content/<stack>-<slug>` form - that's a label only, it doesn't need to match the folders.)

Reference pairs to copy from - see CONTRIBUTING.md for which shape fits:

- `_labs/spring-boot/explain-repo.md` - stack-tied, single prompt
- `_labs/bob-features/generate-architecture-diagram.md` - `stack: Any`, single prompt
- `_labs/bob-features/plan-then-build.md` - multi-step, `### Step N` inside `## Prompt`

## Checklist (both language files)

- [ ] `title` and `lang` (`en` / `ko`) front matter fields set in each file
- [ ] English: `Problem → Prompt → Expected Output → Tips → Variations` - all five sections filled, none skipped
- [ ] Korean: `문제 → 프롬프트 → 기대 결과 → 팁 → 응용` - all five sections filled, none skipped
- [ ] All 5 metadata fields as YAML front matter in each file: `difficulty`, `duration`, `stack`, `work_replaced`, `expected_saving`
- [ ] Each language's Prompt actually run through Bob in that language, Expected Output written from what was observed (not translated from the other file)
- [ ] One asset per commit / PR - both language files together
- [ ] `git status` confirms `NOTE.md` and `docs/` are not staged

The `validate-content` CI check enforces the title/lang/spec/front-matter items and the bilingual pairing automatically.

See `README.md`'s "Content unit spec" and "Contributing" sections, and `AGENTS.md`'s "Adding content", for the full rules.
