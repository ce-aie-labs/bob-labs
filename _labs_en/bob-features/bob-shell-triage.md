---
title: Triage a Log from the Terminal with Bob Shell
lang: en
category: Bob Features
difficulty: Guided
duration: 10 min
stack: Any
work_replaced: Manual log triage
expected_saving: 20 min → 3 min
---

## Problem

A build failed or a service is throwing errors, and you've got a wall of log output in the terminal. Instead of scrolling and grepping by hand, pipe it straight into Bob Shell and get a root-cause read without leaving the command line.

## Prompt

In a terminal, with **Bob Shell** installed, pipe the log into a headless `bob` call:

```
cat error.log | bob "Read this log, tell me the root cause in one or two
sentences, and the single most likely fix. Point at the exact line or
config that's wrong."
```

## Expected Output

- [ ] A root-cause diagnosis in a sentence or two - the actual failing line or condition, not a reworded copy of the error text
- [ ] One most-likely fix named concretely (a specific command, file, or config value), not a list of five things to try
- [ ] It works headlessly: the log is piped in and Bob answers in the terminal, with no IDE and no interactive session, so the same move drops into a script or CI step
- [ ] Bob Shell's stricter defaults hold: if the fix means writing a file or running a command, it asks first rather than acting unprompted

Based on IBM Bob's Level 3 Scale & Extend module, [Bob Shell](https://ibm.github.io/bob-l3/scale/4-2/), which demonstrates headless mode (`cat error.log | bob "explain this"`), the `!` prefix for ordinary shell commands, and the stricter auto-approval defaults. In the Delegate module the same move - handing Bob a 143-line failing log - pinned a stale database file as the real cause.

<!-- Bob-verify: not yet run through Bob. Drafted from the IBM Bob L3 enablement walkthrough (ibm.github.io/bob-l3/scale/4-2/). Bob Shell installs separately and this lab assumes it's set up. Needs a real pass through Bob before use with participants - in particular whether the exact headless `cat ... | bob "..."` invocation works and whether the confirmation-before-write default actually holds. -->

## Tips

- Bob Shell installs separately from the IDE extension and prompts for IBM login on first run. This lab assumes it's already set up - see IBM's product docs for install steps.
- Headless mode is the superpower: `cat <anything> | bob "<question>"` turns Bob into a scriptable component. Pipe compiler output, a stack trace, or a log for triage; generate a summary as a build step.
- Inside an interactive Bob Shell session, prefix a line with `!` to run an ordinary shell command (`!git diff`), then press `Esc` to return to Bob.
- Run several Bob Shell instances in parallel for independent tasks - one triaging logs, one writing tests - trivial in a terminal, awkward in the IDE.

## Variations

1. **Diagnose a failing test run**: `npm test 2>&1 | bob "which test failed and why - name the assertion and the fix"`
2. **Explain a diff before committing**: `git diff | bob "summarize what changed and flag anything risky"`
3. **Fix or escalate**: `cat error.log | bob "if this is a one-line fix, propose the exact change; if it's deeper, say what to investigate first"`
