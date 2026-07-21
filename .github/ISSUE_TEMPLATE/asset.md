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

## Where the file goes

`content/<stack>/<slug>.md` - see `content/spring-boot/explain-repo.md` for a complete reference example.

## Checklist

- [ ] `Problem → Prompt → Expected Output → Tips → Variations` - all five sections filled, none skipped
- [ ] All 5 metadata fields as YAML front matter: `difficulty`, `duration`, `stack`, `work_replaced`, `expected_saving`
- [ ] Prompt actually run through Bob, Expected Output written from what was observed
- [ ] One asset per commit / PR
- [ ] `git status` confirms `NOTE.md` and `docs/` are not staged

See `README.md`'s "Content unit spec" and "Contributing" sections, and `AGENTS.md`'s "Adding content", for the full rules.
