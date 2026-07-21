---
title: Review My Own Changes First
lang: en
category: Code Review
difficulty: Challenge
duration: 15 min
stack: Any
work_replaced: Pre-review of your own PR
expected_saving: 40 min → 10 min
---

## Problem

You are about to open a PR. Half of what a reviewer will flag is findable before you ask for their time - you just have to look with fresh eyes, which is exactly what you no longer have after writing it.

## Prompt

**This is a challenge lab: the goal is given, the prompt is not.** Write your own, run it, and see whether the output matches the Expected Output checklist below. Open a hint only if you are stuck - the point is the practice, not the answer.

Goal: get Bob to review your uncommitted changes the way the reviewer on your PR would, and tell you which findings actually block the merge.

<details markdown="1">
<summary>Hint 1 - the shape of a good prompt</summary>

```
Review <what exactly> as if <who you want Bob to be>.

Look for <the concerns you care about, in priority order>.

For each issue: <what you want back for each one>.

Skip <what you do not want to hear about>.
```

The last line matters more than it looks. Without it you get formatting opinions.

</details>

<details markdown="1">
<summary>Hint 2 - a prompt that works</summary>

```
Review my uncommitted changes as if you were the reviewer on this pull request.

Look for correctness and edge cases first, then error handling, then anything
that breaks a convention already used elsewhere in this repo.

For each issue: name the file and line, say what actually goes wrong, and rate
it blocking / worth fixing / optional.

Skip formatting and style nits - the linter covers those.
```

</details>

## Expected Output

- [ ] Findings tied to specific files and lines, not general advice you could have read in a blog post
- [ ] A severity on each one, so you can tell what blocks the merge from what is a preference
- [ ] An explanation of what actually breaks - "this throws when `items` is empty", not "consider adding a check"
- [ ] Nothing about formatting or import order, because the prompt ruled it out

<!-- Bob-verify: written from the content spec, not yet run through Bob. Needs a real pass on a genuine diff before use with participants - in particular whether the severity rating is consistent enough to be useful. -->

## Tips

- A wall of style nits means the "skip formatting" instruction is missing or too weak. Name what you want ignored explicitly.
- Run this **before** committing. Findings are cheap to act on while the change is still in your working tree and expensive after review has started.
- If the repo has an `AGENTS.md`, Bob reviews against your conventions rather than generic ones. Run `/init` first if there isn't one.

## Variations

1. **Review a whole branch**: "Review every change on this branch against `main`, not just uncommitted files."
2. **One concern only**: "Review these changes for security issues only - injection, authz, and anything that logs a secret."
3. **Turn it into the PR description**: "...and write the PR description, listing what changed and what a reviewer should look at closely."
