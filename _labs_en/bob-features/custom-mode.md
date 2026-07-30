---
title: Build a Reusable Custom Mode
lang: en
category: Bob Features
difficulty: Guided
duration: 10 min
stack: Any
work_replaced: Hand-configuring a workflow persona
expected_saving: 30 min → 5 min
---

## Problem

You keep re-explaining the same role to Bob - "act as a reviewer, only read and comment, don't touch my files." A Custom Mode captures that persona once: a role, instructions, and a locked-down tool set Bob can't exceed. Commit it, and the whole team gets the same specialized Bob in their mode selector.

## Prompt

In **Agent mode**, run:

```
Create a custom mode for code review and write it to a .bobmodes file in
the project root so the team can share it.

- Slug: reviewer
- Role: a senior reviewer who reads changes and reports issues by file and
  line, rated blocking / worth fixing / optional, and never edits code
- When to use: reviewing a diff or a PR before it merges
- Tools: allow Read only. No Edit, no Execute, no MCP - it must not be able
  to change files or run commands.

Explain how the tool restriction is actually enforced.
```

## Expected Output

- [ ] A `.bobmodes` file is written in the project root defining the mode's slug, name, description, role definition, when-to-use, custom instructions, and tools - the full field set, not just a prose blurb
- [ ] The Tools field grants **Read only** - Edit, Execute, and MCP are absent - and Bob explains that the tool list is deterministically enforced, so an instruction to edit would simply be ignored in this mode
- [ ] Switching into the reviewer mode and asking it to fix something is deflected to reporting, because the mode literally cannot write files - the safety comes from the tool set, not the prompt wording
- [ ] The `.bobmodes` file is committable, so every teammate who clones the repo gets the same reviewer persona - it isn't a setting each person re-creates by hand

Based on IBM Bob's Level 3 Tailor module, [Custom Modes](https://ibm.github.io/bob-l3/tailor/3-3/), which builds a Product Manager mode (Read / Edit / MCP allowed, Execute excluded) and stresses that the deterministic Tools field - not the instructions prose - is what constrains what Bob can do.

<!-- Bob-verify: not yet run through Bob. Drafted from the IBM Bob L3 enablement walkthrough (ibm.github.io/bob-l3/tailor/3-3/), which builds the mode through the settings UI rather than by prompt. Needs a real pass through Bob before use with participants - in particular whether Bob writes a valid .bobmodes from this prompt, and whether the Read-only tool set actually blocks an edit request once you switch into the mode. -->

## Tips

- The Tools field is the real safety mechanism, and the only deterministic one. A custom instruction asking for a tool that isn't in the list is silently ignored - so lock down tools rather than relying on "please don't edit."
- You can also build a mode through the settings UI (mode selector → cog → `+`), but asking Bob to generate the `.bobmodes` file gives you a committable artifact and a starting point to tweak by hand.
- Scope matters: a project-scoped mode lives in `.bobmodes` and is shared through the repo; a global mode is available in every project but isn't committed with the code.

## Variations

1. **A planning / PM mode**: "...a `product-manager` mode that turns a fuzzy idea into an MVP card, a now/next/later roadmap, and user stories - Read, Edit, and MCP allowed, Execute excluded."
2. **A docs-writer mode**: "...a `docs` mode that can read code and write Markdown but never run commands - Read and Edit only."
3. **Tighten an existing mode**: "Review my `.bobmodes` and remove any tool a reviewer mode shouldn't have, explaining each removal."
