---
title: Encode Your Team's Standards as Bob Rules
lang: en
category: Bob Features
difficulty: Guided
duration: 10 min
stack: Any
work_replaced: Restating team standards each prompt
expected_saving: 15 min → 3 min
---

## Problem

You keep re-typing the same standards into every chat - "add docstrings," "keep it concise," "log what you changed." Rules are plain-text files Bob loads into every conversation, so a standard is stated once and enforced automatically, for everyone on the repo.

## Prompt

In **Agent mode**, run:

```
Add the following rules to @/.bob/rules/team_rules.md and configure them
so they take effect on every conversation:

- Every public function gets a concise docstring (one sentence is enough).
- Keep comments, docstrings, and prose brief - short, direct sentences.
- After each interaction, write a short summary to an internal-monologue/
  folder, named with a timestamp and a description, e.g.
  2026-01-15_update-readme.md
```

Approve the file writes, then open `.bob/rules/team_rules.md` to see how Bob structured them.

## Expected Output

- [ ] A `.bob/rules/team_rules.md` file is created (Bob makes the folder if it's missing), with the three rules turned into clear, sectioned instructions - not just your text pasted back verbatim
- [ ] The rules apply across every mode (Agent, Plan, Ask) and every new conversation without you restating them, because Bob reads everything in `.bob/rules/` at the start of each chat
- [ ] The internal-monologue rule produces a real audit trail: a timestamped Markdown file per interaction recording what was asked and what was done
- [ ] Because `.bob/rules/` is committed to the repo, the standard propagates to every teammate who clones it and rides along through normal code review - not a setting each person re-configures

Based on IBM Bob's Level 3 Tailor module, [Bob Rules](https://ibm.github.io/bob-l3/tailor/3-2/), which sets up these exact three rules on the Galaxium Travels repo and shows the resulting `internal-monologue/` audit folder, plus the project-vs-global scope distinction.

<!-- Bob-verify: not yet run through Bob. Drafted from the IBM Bob L3 enablement walkthrough (ibm.github.io/bob-l3/tailor/3-2/). Needs a real pass through Bob before use with participants - in particular whether the rules are honored across modes without a nudge, since the walkthrough notes workspace-level rules are sometimes overlooked and may need re-prompting. -->

## Tips

- If your rules aren't being followed, they may not be wired in properly. Tell Bob: "my project rules in `@/.bob/rules/team_rules.md` aren't being applied, configure them properly." Global and agent-scoped rules tend to be honored more strictly than plain workspace ones.
- Keep each rule to one clear line. A rule Bob has to interpret is a rule it will interpret loosely.
- The internal-monologue pattern is the sleeper feature: an audit trail of every change, continuity Bob can refer back to later, and transparency for teammates in a shared repo.
- Project rules (`.bob/rules/`) ship with the repo; global rules live in your user directory and apply to every project. Put team conventions in the project, personal preferences in global.

## Variations

1. **A checklist as a rule**: "Add a rule that any change touching `<area>` must include a test and update the relevant doc."
2. **Communication style**: "Add a rule: explain changes in plain language first, code second, with no more than three sentences of preamble."
3. **Promote to global**: "Move the internal-monologue rule to my global rules so it applies to every project, not just this one."
