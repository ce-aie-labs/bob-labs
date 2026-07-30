---
title: Edit Code Inline with Literate Coding
lang: en
category: Bob Features
difficulty: Guided
duration: 5 min
stack: Any
work_replaced: Manual in-place edits
expected_saving: 20 min → 5 min
---

## Problem

You need a small, surgical change to code that already works - wrap a flaky call in retry logic, add a validation guard - and you don't want an agent restructuring the whole file to do it.

## Prompt

Literate coding is an instruction you write *inside the file*, not in the sidebar. Put your cursor where the change belongs, turn on Literate Coding (the magic wand icon, or `Cmd + I`), and type the instruction inline - directly above the code it applies to:

```
The call below can fail intermittently. Wrap it in retry logic with a
few attempts and exponential backoff, and leave the surrounding code
unchanged.
```

Then click **Generate** (or `Cmd + Enter`), read the diff, and **Accept**.

## Expected Output

- [ ] The change is shown as an in-place diff - added lines in green, removed in red - not a silent rewrite, so you can read exactly what changes before anything is saved
- [ ] The edit is scoped to where you put the cursor: the retry wrapper appears around the target call and the rest of the file is untouched, rather than a whole-file reformat
- [ ] Real, compilable code in your file's language is generated - not a comment or a `TODO` - and the natural-language instruction line itself is removed when you accept
- [ ] You can Accept All, or step through and reject individual blocks - nothing is written to the file until you say so

Based on IBM Bob's Level 3 Assist module, [Literate Coding](https://ibm.github.io/bob-l3/assist/1-1/), which walks this through on the Galaxium Travels demo by wrapping a flaky flight-fetch call in a `loadFlightsWithRetry` helper and adding email validation from a one-line inline comment.

<!-- Bob-verify: not yet run through Bob. Drafted from the IBM Bob L3 enablement walkthrough (ibm.github.io/bob-l3/assist/1-1/). Needs a real pass through Bob before use with participants - in particular whether the inline instruction line is removed on accept and the diff stays scoped to the cursor as described. -->

## Tips

- Literate coding is for focused, single-file edits. For a change that spans multiple files or restructures architecture, reach for Agent mode instead - forcing it here fights the tool.
- If the generated code doesn't compile, compare it against what you expected before re-running. Because generation is non-deterministic, a single Generate can produce a one-off bad output that a re-run fixes.
- Queue several edits at once: drop an instruction line at multiple spots in the same file, then Generate once to apply them all together.

## Variations

1. **Add a guard clause**: "Validate that `<input>` is well-formed before it's used, and return early with a clear error if it isn't."
2. **Add error handling**: "Wrap this block in try/catch and log the error with enough context to debug it, without swallowing it."
3. **Extract a helper**: "Pull this logic out into a well-named helper function, leaving the behavior identical."
