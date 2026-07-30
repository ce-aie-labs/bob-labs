---
title: Give Bob Durable Project Context with /init
lang: en
category: Bob Features
difficulty: Guided
duration: 5 min
stack: Any
work_replaced: Re-explaining the codebase each session
expected_saving: 20 min → 2 min
---

## Problem

Every new Bob conversation starts blank - it rediscovers your project from scratch, re-reading files and burning time and tokens before it does any actual work. `/init` captures that knowledge once so it loads automatically at the start of every conversation.

## Prompt

In **Agent mode**, run:

```
/init
```

Approve the file-write requests as they come. Then open the generated `AGENTS.md` and skim it.

## Expected Output

- [ ] A root `AGENTS.md` that describes *your* project specifically - the real stack, the actual commands to build/test/run (and which subdirectory each runs from), and the directory layout - not generic boilerplate
- [ ] It captures non-obvious gotchas a newcomer would trip on (a column that no longer exists, tests that must patch two places, a cancel flow that's a POST not a DELETE), not just the happy path
- [ ] Mode-specific rule files are also written under `.bob/` (`rules-agent/`, `rules-ask/`, `rules-plan/`), each tuned to what that mode needs - architecture constraints for Plan, coding gotchas for Agent
- [ ] From the next conversation on, Bob answers questions about your project without re-reading everything first - confirm by asking a structure question in a fresh chat and watching it skip the re-scan

Based on IBM Bob's Level 3 Tailor module, [/init and AGENTS.md](https://ibm.github.io/bob-l3/tailor/3-1/), where running `/init` on the Galaxium Travels repo produced `AGENTS.md` plus three mode-specific files - surfacing gotchas like "there is no `seats_available` column anymore" and "MCP tools bypass FastAPI dependency injection."

<!-- Bob-verify: not yet run through Bob. Drafted from the IBM Bob L3 enablement walkthrough (ibm.github.io/bob-l3/tailor/3-1/). Needs a real pass through Bob before use with participants - in particular whether /init writes exactly the four files described and whether the mode-specific files land under .bob/ with those names. -->

## Tips

- Re-run `/init` after a big change (a new module, a restructured tree, a new framework) so the context doesn't go stale - it's the one command that keeps Bob's map of your repo current.
- The generated `AGENTS.md` is a normal file you can hand-edit. Add business rules or deployment conventions that an automated scan would miss.
- On a large repo this isn't optional: without an `AGENTS.md`, Bob spends its context window re-reading files at the start of every conversation instead of doing your task.

## Variations

1. **Refresh after changes**: "Re-run `/init` and update `AGENTS.md` to reflect the new `<module>` I just added."
2. **Add house rules by hand**: after `/init`, "Add a section to `AGENTS.md` documenting our deployment steps and our rule about never editing generated files."
3. **Prove it landed**: in a new chat, "Where would a new `<feature>` go?" - a good `AGENTS.md` lets Bob answer without re-reading the whole tree.
