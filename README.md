# IBM Bob Labs

> This is not a training site.
> It is a **playbook for replacing repetitive work with Bob**, validated through a hackathon.

The goal is for participants to reopen this site the week *after* the event.
If it is a one-off training handout, it failed.

---

## What we are building

| Stage | When | What happens |
|---|---|---|
| 1. Accumulate assets | Before the event | Build Guided Assets that produce results by simply following along |
| 2. Apply assets | Day of the event | Participants replace their own real repetitive work with Bob |
| 3. Feed back | After the event | Participant output is refined and folded back into the asset library |

Every run of the event grows the asset base. That is what separates this from one-off training.

---

## Design principles

Every content decision is made against these five.

1. **Start with copy-paste** — the shorter the time to first success, the better.
2. **Must be measurable** — every lab converts to a Before/After in minutes.
3. **Must be reusable** — an example that only works in its own context is not an asset.
4. **Judged on applicability, not correctness** — can someone else pick this up as-is?
5. **A cookbook, not a lab** — not a curriculum to work through in order, but recipes to look up when needed.

---

## Site structure

```
🏠 Home                    Entry point that delivers first success in 10 minutes
⚙️  Setup (Step 0)          Install · login · permissions · one first prompt
📖 Guided Labs (Step 1)     10–20 copy-paste walkthroughs
🧩 Challenge Labs (Step 2)  Goal only, no prompt provided
📚 Asset Library (Step 3)   Pick a stack, get that stack's lab set
💡 Prompt Cookbook          Prompts by situation (one copy button)
🚀 Bring Your Own Work      Participants run their own repetitive work through Bob
🏆 Showcase                 Share · evaluate · feed back into the asset library
```

---

## Content unit spec

Every Lab / Recipe follows this shape. **No exceptions.**

```
Problem          When do you use this (1–2 lines)
Prompt           [Copy] button. Works as-is when pasted
Expected Output  Checklist of what Bob should give back
Tips             Where it commonly goes wrong, and how to correct it
Variations       2–3 ways to adapt it
```

Metadata, shown as badges on the card and used for filtering. Stored as YAML front matter at the top of the asset file, parsed by the Jekyll build:

```yaml
---
title: Explain This Repository   # the page heading
difficulty: Guided                # Guided / Challenge
duration: 5 min
stack: Java, Spring Boot
work_replaced: Code review
expected_saving: 30 min → 7 min
---
```

Challenge Labs present the Goal only and push hints to the Prompt Cookbook.

---

## Asset Library priority

**Build P0 first. Trying to fill everything finishes nothing.**
P0 alone is enough to run the first event (~40 labs).

| Domain | P0 | P1 | P2 |
|---|---|---|---|
| Java | Spring Boot (8), Legacy Monolith (6) | Maven/Gradle (4) | |
| Python | FastAPI (6) | Data Pipeline (5) | Flask (4) |
| Frontend | React (5) | | Angular (4), Vue (4) |
| DevOps | | Docker (5), Kubernetes (6) | Terraform (4) |
| AI | | RAG (5) | LangGraph (4), MCP (4) |
| Database | | PostgreSQL (5), Oracle (4) | MongoDB (4) |
| Documents | Excel (4), API Spec (5) | PDF (4) | |

Numbers in parentheses are target lab counts.

---

## Prompt Cookbook categories

Target is 20 prompts per category. Each prompt is **one copy button** — never make people read an explanation first.

```
Repository Understanding   Code Review      Test Generation
Migration                  Debug            Performance
Security                   Documentation    Architecture
Meeting Summary            PR Review        README Writing
```

---

## Running the hackathon

There is one sentence we give participants:

> **"Bring the work you do over and over."**

### Timeline (full day, 6.5 hours)

| Time | Session |
|---|---|
| 0:00–0:30 | Setup + first success — clear every environment problem here |
| 0:30–1:30 | Guided Labs |
| 1:30–2:15 | Challenge Labs |
| 2:15–2:30 | Pick your work |
| 2:30–5:00 | **Bring Your Own Work** (the main event) |
| 5:00–5:30 | Write up submission (Before/After) |
| 5:30–6:30 | Showcase |

For a half-day event, drop Challenge Labs.

### Submission

Keep the burden minimal. Only one thing is required.

```
Usually 30 min  →  Bob 7 min
```

If there is time, add `Prompt → Output → Screenshot → Reflection`.

### Rubric

| Criterion | Points |
|---|---|
| Was it genuinely repetitive work | 20 |
| Was Bob used well | 30 |
| Was there real time saved | 20 |
| Can someone else reuse it | 30 |

The weighting toward reusability (30) and applicability (30) is deliberate.
What this event produces is not a ranking of people — it is **assets**.

---

## Roadmap

- **Phase 1 — Skeleton**: Setup page · 10 Guided Labs · 3 Cookbook categories · Bring Your Own Work template · rubric
- **Phase 2 — Asset build-out**: All P0 (~40 labs) · 5 Challenge Labs · all Cookbook categories · Showcase
- **Phase 3 — Ongoing operation**: Feedback pipeline · P1/P2 assets · stack filters and search

Current status: **Phase 1 started** — Jekyll site scaffolding, deployed to GitHub Pages via GitHub Actions, live with the first lab.

---

## Open decisions

These need to be settled before real work starts.

- [ ] **Bob's actual capability surface** — agent execution / context injection / external tool integration. Settle this before producing assets; it determines lab difficulty and subject matter.
- [ ] **Target audience** — internal developers or customers? This decides whether we supply practice repos or use participants' own code.
- [ ] **Practice repositories** — build one per stack, or point at public open source?
- [ ] **Event size and duration** — the timeline above assumes a 6.5-hour day.
- [x] **Site implementation** — Jekyll, built and deployed via GitHub Actions to GitHub Pages (not GitHub's legacy auto-build, so we aren't limited to the `github-pages` gem's plugin whitelist). Adding an asset is a single PR that lands on the site automatically once merged.

---

## Repository

```
README.md    This document — project overview
AGENTS.md    Working rules for coding agents
CLAUDE.md    Imports AGENTS.md for Claude Code
NOTE.md      Original planning note (local only, gitignored)
docs/        Design system and reference docs (local only, gitignored)
_labs/       Lab / Recipe assets (a Jekyll collection): _labs/<stack>/<slug>.md
_layouts/    Jekyll page templates
_includes/   Jekyll partials (e.g. the lab card)
assets/      Site CSS and fonts
```

Design follows the IBM Carbon Design System (`docs/DESIGN.md`).

Every asset lives at `_labs/<stack>/<slug>.md` — e.g. `_labs/spring-boot/explain-repo.md`.
See that file for a complete reference example of the content spec below, fully filled in.
The `_labs/` underscore prefix is required (it's how Jekyll recognizes the collection) — the asset's *branch* name still uses the hyphenated `content/<stack>-<slug>` form, that's just a label and doesn't need to match the folder.

---

## Contributing

Every change goes through a PR. `main` is never committed to directly.

1. **Branch off `main`.**
   `content/<stack>-<slug>` for assets — e.g. `content/spring-boot-explain-repo`.
   `feat/`, `fix/`, `docs/` for everything else.
2. **One asset per commit.** Don't mix several labs into one commit — each asset should be reviewable, and revertable, on its own.
3. **Fill the content spec completely.** `Problem → Prompt → Expected Output → Tips → Variations`, plus all five metadata fields and `title`. A section or field left blank fails the `validate-content` check and blocks the PR.
4. **Verify the prompt against Bob before opening the PR.** Expected Output describes what you actually saw, not what you expect Bob to do.
5. **Open the PR:** `git push -u origin <branch>` then `gh pr create`. Requires one approval and passing status checks before it can merge.

Commit messages are in English, imperative mood.

### Before you commit

`NOTE.md` and `docs/` are local-only working documents and are gitignored.
Run `git status` and confirm neither is staged.

### What gets merged

A PR is judged by the same checklist we use for the content itself:

- Does copy-paste produce a first result within 5 minutes?
- Does it convert to a Before/After in minutes?
- Can someone else pick it up as-is?
- Is it P0? While P0 items remain, P1/P2 waits.

Reusability beats asset count. A rejected lab is cheaper than a lab nobody reopens.
