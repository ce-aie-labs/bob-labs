---
title: Document a File in One Pass
lang: en
category: Documentation
difficulty: Guided
duration: 10 min
stack: Any
work_replaced: Manual documentation
expected_saving: 45 min → 10 min
---

## Problem

A file's public functions have no docstrings, or a README has drifted from what the code actually does. Documenting by hand is low-reward busywork that never quite reaches the top of the list.

## Prompt

In **Agent mode**, run:

```
Add a concise docstring to every public function in @/<path/to/file> - one
or two sentences saying what it does, its parameters, and what it returns
or raises. Match the docstring style already used elsewhere in this repo,
and don't change any behavior or logic.

Skip trivial private helpers, and don't add noise comments that just
restate the code.
```

## Expected Output

- [ ] A docstring on each public function - what it does, its parameters, and its return/raise behavior - in the repo's existing style (JSDoc, Python docstrings, whatever's already there), not a foreign convention
- [ ] No behavior changed: the diff is comments and docstrings only, so it's safe to merge without a full re-review of the logic
- [ ] It skips the noise - no `// increment i` comments restating obvious code, and trivial private helpers left alone
- [ ] Where a function's purpose is genuinely unclear from the code, Bob flags it or asks rather than inventing a confident-but-wrong description

"Automates the busy-work of development: from documentation, to commit messages, to test scaffolding" is one of the three core strengths IBM Bob's [Level 3 course](https://ibm.github.io/bob-l3/) names. The Tailor module encodes exactly this as a rule ("a concise docstring on every public function"), and the Bob Shell module opens by asking Bob to "check the readme and see if you can make any improvements."

<!-- Bob-verify: not yet run through Bob. Drafted from the IBM Bob L3 course's stated strengths (ibm.github.io/bob-l3/), its Bob Rules docstring example (tailor/3-2), and the Bob Shell readme example (scale/4-2). Needs a real pass through Bob before use with participants - in particular whether the diff stays docs-only and whether Bob matches the repo's existing style rather than imposing one. -->

## Tips

- Constrain it to docs-only explicitly ("don't change behavior"). An open documentation prompt invites Bob to "improve" the code too, turning a safe comment-only diff into a real review.
- Match the existing style or you'll get inconsistency. If the repo uses JSDoc, say so; if half the file is already documented, tell Bob to follow that.
- The best docstring says *why*, not *what*. If Bob's output just restates the function name in a sentence, ask it to explain intent and gotchas instead.
- Pair it with a rule (see the Bob Rules lab) so new code gets documented automatically, instead of running this on every file after the fact.

## Variations

1. **Refresh a README**: "Read `@/<module>` and update `README.md` so the usage examples and commands match what the code actually does now."
2. **Explain a gnarly function**: "Add a docstring to `<function>` that explains the non-obvious algorithm and why it's written this way, not just its signature."
3. **Generate a module overview**: "Write a short `README.md` for the `@/<folder>` package: what it's for, the main entry points, and how the pieces fit together."
