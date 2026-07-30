---
title: Root-Cause an Error from Its Stack Trace
lang: en
category: Debug & Performance
difficulty: Guided
duration: 5 min
stack: Any
work_replaced: Debugging by guesswork
expected_saving: 30 min → 5 min
---

## Problem

Something threw, you've got a stack trace or a wall of failing output, and you're one wrong guess away from an hour spent debugging the symptom instead of the cause. Hand Bob the actual error and let it trace it back to the real problem.

## Prompt

In **Agent mode**, run:

```
Here's the error I'm getting:

<paste the full stack trace / failing output>

Find the root cause, not just where it surfaced. Read the relevant code,
tell me what's actually wrong in one or two sentences, and propose the
smallest fix. If the trace points at a symptom, trace it back to the cause.
```

## Expected Output

- [ ] The real root cause named - the underlying problem, not just the line the trace points at, which is often only where the symptom surfaced
- [ ] Bob reads the actual relevant code to confirm the cause, rather than pattern-matching the error string to a generic search-result answer
- [ ] The smallest fix that addresses the cause, with the specific file, line, or command - not a rewrite and not five speculative things to try
- [ ] If the fix is risky or more than one cause is plausible, Bob says so and tells you what to check, instead of committing confidently to a guess

In IBM Bob's Level 3 Delegate module, pasting a 143-line failing startup log to Bob ([Applying Agentic Modes](https://ibm.github.io/bob-l3/delegate/2-2/)) traced a broken feature back to a stale `booking.db` file - a schema-error symptom resolved to its cause: SQLAlchemy's `create_all` never alters existing tables. The Scale module generalizes this into headless triage (`cat error.log | bob "explain this"`).

<!-- Bob-verify: not yet run through Bob. Drafted from the IBM Bob L3 debugging episode (ibm.github.io/bob-l3/delegate/2-2/) and the headless-triage module (scale/4-2). Needs a real pass through Bob before use with participants - in particular whether Bob reaches the underlying cause rather than restating the trace, and whether the proposed fix is genuinely minimal. -->

## Tips

- Paste the *whole* trace, not just the last line. The bottom of a stack trace is where the error surfaced; the cause is usually higher up, or in code the trace only hints at.
- Ask for the root cause explicitly. "Fix this error" tends to patch the symptom (swallow the exception); "find the root cause" makes Bob trace it back.
- Give it reproduction context when you have it - what you did, what you expected, the environment. A schema error that's really a stale-database problem is only obvious with the surrounding facts.
- For a fast loop, pipe it in from the terminal instead: `cat error.log | bob "root cause and smallest fix"` (see the Bob Shell lab).

## Variations

1. **Performance, not a crash**: "This endpoint is slow under load - here's a profile / timing. Find the bottleneck and the smallest change that fixes it."
2. **Flaky, not consistent**: "This test fails about one run in five. Here are two traces from failing runs - what race or shared state is causing it?"
3. **Explain before fixing**: "Just explain what's causing this and why - don't change anything yet, I want to understand it first."
