---
title: Understand Code Safely in Ask Mode
lang: en
category: Bob Features
difficulty: Guided
duration: 5 min
stack: Any
work_replaced: Manual code reading
expected_saving: 30 min → 5 min
---

## Problem

You need to understand a piece of code before you touch it - but you're wary of an agent "helpfully" editing files or running commands while you're only trying to read. Ask mode is read-only: Bob explains and traces, and can't change or run anything.

## Prompt

Switch to **Ask mode** (mode selector), then point Bob at the exact code with an `@/path` mention:

```
Explain how @/<path/to/entry-point> works end to end: what it does, which
functions or modules it calls, where the important state lives, and the one
part most likely to surprise someone changing it for the first time.
```

## Expected Output

- [ ] A grounded explanation naming the actual functions, files, and data flow - traced from the entry point you pointed at, not a generic account of "how apps like this usually work"
- [ ] It calls out where state lives and the non-obvious part someone would trip on, rather than only narrating the happy path
- [ ] No files are changed and no commands are run - the selector shows Ask, and Bob has no ability to edit or execute even if you ask it to mid-answer
- [ ] Because you used an `@/path` mention, the answer is anchored to the specific code you named instead of Bob guessing which file you meant

Based on IBM Bob's Level 3 Delegate module, [The Agentic Sidebar](https://ibm.github.io/bob-l3/delegate/2-1/), which describes Ask mode as a read-only, explanation-focused mode for understanding code without making changes, and context mentions (`@/path/to/file`, `@problems`, `@terminal`) for feeding Bob precise context.

<!-- Bob-verify: not yet run through Bob. Drafted from the IBM Bob L3 enablement walkthrough (ibm.github.io/bob-l3/delegate/2-1/). Needs a real pass through Bob before use with participants - in particular whether Ask mode truly blocks edits and command execution, and whether the @/path mentions resolve to the intended files as described. -->

## Tips

- Ask mode is the safe default for reading unfamiliar or sensitive code: it can't accidentally edit or run anything, so you can explore freely.
- Point at exactly what you mean with mentions: `@/path/to/file` pulls in one file, `@/path/to/folder` a whole directory, `@problems` the current diagnostics, `@terminal` recent output.
- When your question turns into "now change it," switch to Agent mode deliberately - the mode boundary is your reminder that you're moving from reading to writing.
- Without an `AGENTS.md`, an Ask answer on a large repo can stay shallow. Run `/init` first for a deeper, more accurate explanation.

## Variations

1. **Trace one path**: "In Ask mode, walk through exactly what happens when `<action>` is triggered, function by function, starting from `@/path/to/entry`."
2. **Explain a diagnostic**: "Explain what `@problems` is telling me and what's causing each one - don't fix anything yet."
3. **Compare two files**: "Explain how `@/path/a` and `@/path/b` differ in approach, and when I'd reach for each."
