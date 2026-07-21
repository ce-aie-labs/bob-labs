---
title: Plan a Feature, Then Build It
lang: en
category: Bob Features
difficulty: Guided
duration: 30 min
stack: Any
work_replaced: Feature planning and implementation
expected_saving: 2 hours → 30 min
---

## Problem

You need to add a feature that touches several layers (UI, API, database), and letting an agent start coding immediately gives you scope creep and files you didn't ask for. This is the multi-step workflow that puts a reviewable spec between you and the code.

## Prompt

This lab is four prompts, not one. Run them in order and check the result of each before moving on.

### Step 1 - Give Bob the project context

In **Agent mode**, run:

```
/init
```

**Checkpoint:** Bob writes a root `AGENTS.md` describing your project's structure and conventions, plus per-mode rule files under `.bob/`. Skim the root file - if it describes your project wrongly, fix it now, because every later step builds on it.

### Step 2 - Write a scoped plan prompt

Switch to **Plan mode** (mode selector, or type `/plan`). Fill in the template below and run it. The four blocks are what keep the plan tight - drop them and you get a vague plan.

```
I want to add <feature> to this project. <One sentence on what a user can do once it works.>

Scope:
- <layer 1 change, e.g. update the booking UI to show a selector>
- <layer 2 change, e.g. update the API to accept and store the value>
- <layer 3 change, e.g. add a column to the database schema>
- Do not <explicitly excluded work> - that is out of scope

Constraints:
- Save the plan in a `plans` folder
- Do not create more than three plan files
- <any non-negotiables: backward compatibility, naming conventions, no new dependencies>

Done when: <a concrete, checkable acceptance criterion>
```

**Checkpoint:** Bob may ask clarifying questions first - answer them, or tell it to pick sensible defaults. It then writes plan files into `plans/`. No code has been written yet.

### Step 3 - Review the plan, then fix it

Open the plan files and read them against three specific things:

- **Scope match** - is everything you asked for there, and nothing you didn't?
- **Vague language** - phrases like "update appropriately" or "add as needed" are unstated assumptions that become bad code later.
- **Named files** - each step should name the files it touches. "Update the booking component" is not specific enough.

Stay in the same Plan mode conversation and correct what you found:

```
Update the plan to <specific correction>.
```

**Checkpoint:** The revised plan names concrete files and has no hedging language. Repeat until it does. This is the cheapest place in the whole workflow to catch a mistake.

### Step 4 - Implement from the approved plan

Start a **new conversation** (the plus icon), then switch to **Agent mode** and run:

```
Build the features in the plans folder
@plans/
```

**Checkpoint:** Bob works from the plan rather than re-deriving intent. Approve the tool requests as they come.

## Expected Output

- [ ] After Step 1, a root `AGENTS.md` that accurately describes *your* project - not generic boilerplate
- [ ] After Step 2, plan files in `plans/` and **no source code changed yet** - the whole point is that planning is reversible and cheap
- [ ] A plan whose steps name actual files in your repo, with the excluded scope genuinely absent
- [ ] After Step 4, an implementation that follows the reviewed plan, with the acceptance criterion from "Done when" actually met

Drafted from IBM's own published tutorial ([Plan and implement complex features](https://bob.ibm.com/docs/ide/tutorials/create-a-plan-and-implement-complex-features)), generalized from its Galaxium Travels seat-class example into a fill-in template, rather than independently dry-run tested.

<!-- Bob-verify: not yet run - not through Bob, not through Claude Code. Drafted from Bob's own tutorial docs (docs/bob/ide/tutorials/create-a-plan-and-implement-complex-features.md). Needs a real pass through Bob on a genuine feature before use with participants. Pay attention to whether Step 4's fresh-conversation handoff actually works as described - that's the step most likely to differ in practice. -->

## Tips

- **Starting a new conversation before Step 4 is not optional housekeeping.** It clears the context window so the implementation isn't competing with the whole planning discussion for tokens, and Bob can't confuse "things we considered" with "things we decided."
- If Bob's plan is much bigger than you expected, your Scope block was too loose. Tighten the "Do not..." line rather than trying to rein it in during implementation.
- The file-count constraint ("no more than three plan files") is doing real work - without a cap, plans sprawl into documents nobody reads.
- If you only want the plan and not the build, stop after Step 3. A reviewed plan is a useful artifact on its own, for handing to a teammate or pasting into a ticket.

## Variations

1. **Refactor instead of a feature**: same four steps, but Step 2 describes the target state ("split this 800-line controller into service classes, no behavior change") and "Done when" is "all existing tests still pass."
2. **Plan only, as a design doc**: stop after Step 3 and ask "Rewrite the approved plan as a design doc for a teammate who hasn't seen this codebase."
3. **Plan straight into tickets**: after Step 3, "Turn each step of the plan into a separate issue description with acceptance criteria."
