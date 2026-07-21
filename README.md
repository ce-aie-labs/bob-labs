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

Every Lab / Recipe is **bilingual, mandatory** — an English file and a Korean file, added together as one asset. Korean customers paste Bob prompts in Korean, so the Prompt itself is translated too, not just the surrounding explanation.

Both follow the same shape, headings in their own language. **No exceptions, in either language.**

```
English                Korean
Problem                문제
Prompt                 프롬프트
Expected Output        기대 결과
Tips                   팁
Variations             응용
```

Metadata, shown as badges on the card and used for filtering. Stored as YAML front matter at the top of each asset file, parsed by the Jekyll build. Front matter **keys** stay in English in both files — only the values and body content are written in each file's language:

```yaml
---
title: Explain This Repository   # the page heading, in the file's own language
lang: en                          # en or ko - this is what templates/CI key off
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
| Bob Features | Architecture diagrams, subagents, skills, MCP (5) | | |
| Java | Spring Boot (8), Legacy Monolith (6) | Maven/Gradle (4) | |
| Python | FastAPI (6) | Data Pipeline (5) | Flask (4) |
| Frontend | React (5) | | Angular (4), Vue (4) |
| DevOps | | Docker (5), Kubernetes (6) | Terraform (4) |
| AI | | RAG (5) | LangGraph (4), MCP (4) |
| Database | | PostgreSQL (5), Oracle (4) | MongoDB (4) |
| Documents | Excel (4), API Spec (5) | PDF (4) | |

Numbers in parentheses are target lab counts. **Bob Features** is stack-agnostic (`stack: Any`) - it demos what makes Bob itself worth using (subagents, skills, MCP, diagram/report generation), not a language or framework. Every other domain still needs its own "why Bob" moment inside the lab, but this domain is where that's the entire point.

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

- [x] **Bob's actual capability surface** — agent execution / context injection / external tool integration. Reference is [bob.ibm.com/docs/ide](https://bob.ibm.com/docs/ide) and [bob.ibm.com/docs/shell](https://bob.ibm.com/docs/shell) - use it when writing a lab's Prompt/Expected Output. A local Markdown mirror exists at `docs/bob/` for whoever has fetched it (gitignored, not shipped with the repo - `docs/` is local-only, see "Repository" below).
- [ ] **Target audience** — internal developers or customers? This decides whether we supply practice repos or use participants' own code.
- [ ] **Practice repositories** — build one per stack, or point at public open source?
- [ ] **Event size and duration** — the timeline above assumes a 6.5-hour day.
- [x] **Site implementation** — Jekyll, built and deployed via GitHub Actions to GitHub Pages (not GitHub's legacy auto-build, so we aren't limited to the `github-pages` gem's plugin whitelist). Adding an asset is a single PR that lands on the site automatically once merged.
- [ ] **Java Modernization assets** — deferred. Bob's Java Modernization is a paid **premium package** ([bob.ibm.com/docs/ide/premium-packages/java-modernization](https://bob.ibm.com/docs/ide/premium-packages/java-modernization)), not part of base Bob - 4 workflows (Java upgrade, Liberty replatforming, UI modernization, unit test generation). We don't have access yet; revisit once it's confirmed.

---

## Repository

```
README.md       This document — project overview
AGENTS.md       Working rules for coding agents
CLAUDE.md       Imports AGENTS.md for Claude Code
CONTRIBUTING.md Full contributing guide (branches, bilingual asset workflow, PR checklist)
NOTE.md         Original planning note (local only, gitignored)
docs/           Design system + a local Bob docs mirror in docs/bob/ (local only, gitignored)
_labs/          English Lab / Recipe assets (a Jekyll collection): _labs/<stack>/<slug>.md
_labs_ko/       Korean mirror of _labs/, same relative paths
_data/          ui.yml - site-chrome translation strings (badge labels, nav)
_layouts/       Jekyll page templates
_includes/      Jekyll partials (e.g. the lab card)
assets/         Site CSS and fonts
ko/             Korean homepage (index.md), served at /ko/
```

Design follows the IBM Carbon Design System (`docs/DESIGN.md`).

Every asset lives at `_labs/<stack>/<slug>.md` plus its mirror `_labs_ko/<stack>/<slug>.md` — e.g. `_labs/spring-boot/explain-repo.md` and `_labs_ko/spring-boot/explain-repo.md`.
See that pair for a complete reference example of the content spec below, fully filled in.
The `_labs/`/`_labs_ko/` underscore prefixes are required (it's how Jekyll recognizes the collections) — the asset's *branch* name still uses the hyphenated `content/<stack>-<slug>` form, that's just a label and doesn't need to match the folders.

---

## Contributing

Every change goes through a PR - `main` is never committed to directly, and it's enforced by branch protection.

See **[CONTRIBUTING.md](CONTRIBUTING.md)** for the full guide: branch naming, the bilingual asset workflow, front matter fields, and what gets merged. Opening a PR picks up `.github/PULL_REQUEST_TEMPLATE.md` automatically.
