---
title: Scaffold a Test Suite for a Module
lang: en
category: Test Generation
difficulty: Guided
duration: 15 min
stack: Any
work_replaced: Writing tests by hand
expected_saving: 1 hour → 15 min
---

## Problem

A module has no tests, or the ones it has only cover the happy path. Writing a real suite by hand - fixtures, edge cases, the annoying error paths - is exactly the tedious work that keeps getting deprioritized.

## Prompt

In **Agent mode**, run:

```
Write a test suite for @/<path/to/module> using this project's existing
test framework and conventions.

Cover the happy path, the edge cases (empty input, boundaries, invalid
values), and the error paths - not just the obvious case. For each test,
make the assertion check real behavior, not just that the function runs.

Then run the tests and fix any that fail because the test itself is wrong.
```

## Expected Output

- [ ] Tests written in the project's actual framework and style - reusing existing fixtures and helpers, matching how current tests are structured - not a foreign framework bolted on
- [ ] Coverage beyond the happy path: empty input, boundaries, and invalid values each get a case, plus the error/exception paths - not four variations of the same successful call
- [ ] Assertions that check real behavior and specific expected values, not "it returned something" or "it didn't throw"
- [ ] Bob runs the suite and the tests pass - or it explains a genuine bug it surfaced in the code under test, rather than weakening an assertion to force green

Automating test scaffolding is one of the three things IBM Bob's [Level 3 course](https://ibm.github.io/bob-l3/) highlights Bob doing well. The Tailor module's generated `AGENTS.md` also surfaced real test gotchas on the demo repo - backend tests using an outdated model, a frontend with zero tests - the kind of context a good test prompt should respect.

<!-- Bob-verify: not yet run through Bob. Drafted from the IBM Bob L3 course's stated Bob strengths (ibm.github.io/bob-l3/) and its AGENTS.md test notes, not a single step-by-step page. Needs a real pass through Bob before use with participants - in particular whether Bob reuses the project's own framework and whether it fixes failing tests by correcting the test rather than loosening the assertion. -->

## Tips

- Point Bob at the existing tests too ("match the style in `@/tests`"), so it reuses your fixtures and framework instead of inventing a setup.
- Watch for the failure mode where a test is weakened to pass. If Bob makes a test green by loosening the assertion, that's a false pass - ask it to explain why the original assertion failed first.
- Ask for edge cases explicitly. "Write tests" alone tends to produce happy-path coverage; naming empty / boundary / invalid inputs is what forces the useful cases.
- If there's no test setup yet, have Bob scaffold the harness first ("set up the test framework the way this stack usually does"), then generate tests in a second pass.

## Variations

1. **Characterize legacy code**: "Write tests that capture the *current* behavior of `@/<module>` exactly as-is so I can refactor safely - characterization tests, not aspirational ones."
2. **Fill a coverage gap**: "Which branches in `@/<module>` aren't covered by the existing tests? Add just those cases."
3. **Reproduce one bug**: "Write a failing test that reproduces `<bug>`, then fix the code until it passes."
