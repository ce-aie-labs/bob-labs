# IBM Bob Labs

> This is not a training site.
> It is a **playbook for replacing repetitive work with Bob**, validated through a hackathon.

**Live site: <https://ce-aie-labs.github.io/bob-labs/>** - English under `/en/`, Korean under `/ko/`. The bare root redirects to `/en/`; note the `/bob-labs/` base path (it is a GitHub Pages project site, so dropping the prefix 404s).

> **Here to build assets?** Adding a lab is two Markdown files and no code. Jump straight to [Add a lab](#add-a-lab).

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

1. **Start with copy-paste** - the shorter the time to first success, the better.
2. **Must be measurable** - every lab converts to a Before/After in minutes.
3. **Must be reusable** - an example that only works in its own context is not an asset.
4. **Judged on applicability, not correctness** - can someone else pick this up as-is?
5. **A cookbook, not a lab** - not a curriculum to work through in order, but recipes to look up when needed.

---

## Site structure

```
🏠 Asset Library            /en/ and /ko/ - every lab, filtered by work, stack and difficulty
🏃 Bobathon                 /en/bobathon/ (and /ko/) - the event: process, rubric, submission
⚙️  Setup                    Install · login · permissions · one first prompt
💡 Prompt Cookbook          Prompts by situation (one copy button)
```

**Bobathon is the event page**, and it absorbs what earlier drafts split across "Challenge Labs", "Bring Your Own Work" and "Showcase" - one page holding the process a participant follows, how the work is judged, and the submit button. Those were three names for one thing, and a participant mid-session should not have to work out which one they are on.

**Guided and Challenge are lab difficulties, unrelated to the Bobathon.** A `difficulty: Challenge` lab states the goal and hides the prompt behind `<details>` so you write it yourself; it is practice, not the event. The homepage filter surfaces them.

The submit button points at an external form (Microsoft or Google Forms), set in `_config.yml` under `bobathon.submit_url`. GitHub Pages is a static site with no backend, and requiring a GitHub account would exclude the customers this event is for. While the URL is empty the button renders disabled with a note, rather than linking somewhere broken.

---

## Content unit spec

Every Lab / Recipe is **bilingual, mandatory** - an English file and a Korean file, added together as one asset. Korean customers paste Bob prompts in Korean, so the Prompt itself is translated too, not just the surrounding explanation.

Both follow the same shape, headings in their own language. **No exceptions, in either language.**

```
English                Korean
Problem                문제
Prompt                 프롬프트
Expected Output        기대 결과
Tips                   팁
Variations             응용
```

Metadata, shown as badges on the card and used for filtering. Stored as YAML front matter at the top of each asset file, parsed by the Jekyll build. Front matter **keys** stay in English in both files - only the values and body content are written in each file's language:

```yaml
---
title: Explain This Repository   # the page heading, in the file's own language
lang: en                          # en or ko - this is what templates/CI key off
category: Repository Understanding # must match a row in the priority tables below
difficulty: Guided                # Guided / Challenge
duration: 5 min
stack: Java, Spring Boot
work_replaced: Code review
expected_saving: 30 min → 7 min
---
```

**Challenge labs** use the same five sections at `difficulty: Challenge` - they are not a separate content type. Their `## Prompt` section states the goal, then hides the help behind `<details>`: a prompt skeleton first, the full prompt second. Anyone who can write it unaided never opens them; anyone stuck is unblocked in seconds instead of losing the session.

They exist to bridge Guided labs, where the prompt is handed to you, and Bring Your Own Work, where nothing is. That makes them matter **most** for less experienced participants, not least - without the bridge, the jump to your own problem on a blank page is a cliff.

---

## Asset Library priority

**Build P0 first. Trying to fill everything finishes nothing.**

Assets are prioritized by **the repetitive work they replace**, not by tech stack. A participant arrives thinking "I lose three hours a week to code review," not "I want a Spring Boot lab." Stack is a filter, not a category - see [Stack coverage](#stack-coverage) below.

**P0 - build these first.** 18 labs, enough to run the first event. 4 built.

| Work replaced | Target labs | Status and ideas |
|---|---|---|
| Bob Features (`stack: Any`) | 5 | **2 built** - architecture diagrams, plan→build. Ideas: subagents, skills, MCP |
| Repository Understanding | 2 | **1 built** - explain this repository |
| Code Review | 2 | **1 built** - review my own changes (Challenge). Ideas: review someone else's PR |
| Test Generation | 2 | Generate tests for an untested module |
| Documentation | 2 | README, API docs, onboarding docs from code |
| Migration | 3 | Spring Boot 2→3, legacy monolith - genuinely stack-specific |
| Data & Documents | 2 | Excel, API spec |

**P1 - only after P0 is done.** 9 labs.

| Work replaced | Target labs | Ideas |
|---|---|---|
| Debug & Performance | 3 | Trace a failure, find a bottleneck |
| Security | 2 | Audit for common vulnerabilities |
| Migration | 2 | Python 2→3 |
| Data & Documents | 2 | PDF |

"Target labs" is a ceiling, not a quota - three labs people actually reopen beat eight nobody does.

**Bob Features** demos what makes Bob itself worth using - Plan mode, subagents, skills, MCP, diagram and report generation. Every other row still needs its own "why Bob" moment inside the lab, but here that *is* the lab.

### Write stack-agnostic first

Most repetitive work isn't stack-specific. "Explain this repository," "review this diff," "generate tests for this module" are the same lab whether the code is Java or Python - only the repo you point at changes. Write those once with `stack: Any` and put the per-stack differences in the asset's own **Variations** section, which exists for exactly this.

Write a stack-specific lab only when the stack genuinely changes the prompt - a Spring Boot 2→3 migration is real Spring knowledge; "explain this repo" is not.

This is the design principle "an example that only works in its own context is not an asset" applied to the build plan itself. One reusable lab beats five near-duplicates.

### Stack coverage

`stack` is a front matter field, so the site filters on it. Use this as a coverage checklist, not a build queue - the goal is that a participant from any of these can find something, not that each one gets its own lab.

**Must cover:**

| Area | Stacks |
|---|---|
| Java | Spring Boot, legacy monolith |
| Python | FastAPI |
| Frontend | React |
| Data & Documents | Excel, API spec |

**Everything else is out of scope for the first event.** Docker, Kubernetes, Terraform, cloud databases, RAG and LangGraph are advanced infrastructure and AI topics, and the audience for this event is customers whose day-to-day work is the list above. Revisit after the first run, based on what participants actually brought.

---

## Prompt Cookbook categories

Same axis as the Asset Library priority above - the difference is depth, not subject. A **Lab** walks you through a worked example with Expected Output and Tips; a **Cookbook prompt** is one copy button with no walkthrough, for someone who already knows what they want.

Target is 20 prompts per category. Each prompt is **one copy button** - never make people read an explanation first.

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
| 0:00-0:30 | Setup + first success - clear every environment problem here |
| 0:30-1:30 | Guided Labs |
| 1:30-1:50 | Challenge Labs - the bridge to writing your own prompt |
| 1:50-2:05 | Pick your work - **Bobathon** starts here |
| 2:05-5:00 | **Bring Your Own Work** (the main event) |
| 5:00-5:30 | Write up submission (Before/After) |
| 5:30-6:30 | Showcase |

Everything from "Pick your work" onward runs off the [Bobathon page](/en/bobathon/): the process, the rubric, and the submission.

For a half-day event, cut Guided Labs rather than the Challenge block - the bridge is what makes Bring Your Own Work survivable.

### Submission

Keep the burden minimal. Only one thing is required.

```
Usually 30 min  →  Bob 7 min
```

Everything else - Bob chat history, repository link, what went wrong and what fixed it - is optional but is what turns a submission into a reusable lab. The full list and the submit button live on the [Bobathon page](/en/bobathon/).

### Rubric

| Criterion | Points |
|---|---|
| Was it genuinely repetitive work | 20 |
| Was Bob used well | 30 |
| Was there real time saved | 20 |
| Can someone else reuse it | 30 |

The weighting toward reusability (30) and applicability (30) is deliberate.
What this event produces is not a ranking of people - it is **assets**.

---

## Roadmap

- **Phase 1 - Skeleton**: Setup page · 10 Guided Labs · 2 Challenge Labs · 3 Cookbook categories · Bring Your Own Work template · rubric
- **Phase 2 - Asset build-out**: All P0 (18 labs) · more Challenge Labs · all Cookbook categories · Showcase
- **Phase 3 - Ongoing operation**: Feedback pipeline · P1/P2 assets · stack filters and search

Current status: **Phase 1 started** - Jekyll site scaffolding, deployed to GitHub Pages via GitHub Actions, live with the first four labs.

---

## Open decisions

These need to be settled before real work starts.

- [x] **Bob's actual capability surface** - agent execution / context injection / external tool integration. Reference is [bob.ibm.com/docs/ide](https://bob.ibm.com/docs/ide) and [bob.ibm.com/docs/shell](https://bob.ibm.com/docs/shell) - use it when writing a lab's Prompt/Expected Output. A local Markdown mirror exists at `docs/bob/` for whoever has fetched it (gitignored, not shipped with the repo - `docs/` is local-only, see "Repository" below).
- [x] **Target audience** - **customers**, and not currently highly technical. This is why P0 skews toward low-floor repetitive work (documents, repository understanding, code review) rather than advanced infrastructure or AI topics, and why everything outside "Must cover" in [Stack coverage](#stack-coverage) is out of scope for the first event.
- [ ] **Practice repositories** - split into two questions. **For authoring the labs: decided** - a fixed shortlist of public repos, one per must-cover stack, listed in [CONTRIBUTING.md](CONTRIBUTING.md#practice-repositories). Every lab runs its Prompt against that shared set, so inputs are consistent and a reviewer can reproduce any Expected Output. **For the event day: still open** - customers may not be able to bring their own code to a shared event for IP reasons, which argues for supplying practice repos on the day; settle once the audience's constraints are known.
- [x] **Challenge Labs** - kept, but as a `difficulty` value rather than a site section, with help behind progressive disclosure instead of withheld entirely. They are the bridge from Guided to Bring Your Own Work, so dropping them would make the main event harder for this audience, not easier. The first-event block is 20 minutes rather than 45, and the 25 minutes go to Bring Your Own Work.
- [ ] **Event size and duration** - the timeline above assumes a 6.5-hour day.
- [x] **Site implementation** - Jekyll, built and deployed via GitHub Actions to GitHub Pages (not GitHub's legacy auto-build, so we aren't limited to the `github-pages` gem's plugin whitelist). Adding an asset is a single PR that lands on the site automatically once merged.
- [ ] **Java Modernization assets** - deferred. Bob's Java Modernization is a paid **premium package** ([java-modernization-index](https://bob.ibm.com/docs/ide/premium-packages/java-modernization/java-modernization-index)), not part of base Bob - 4 workflows (Java upgrade, Liberty replatforming, UI modernization, unit test generation). We don't have access yet; revisit once it's confirmed.

---

## Repository

```
README.md       This document - project overview
AGENTS.md       Working rules for coding agents
CLAUDE.md       Imports AGENTS.md for Claude Code
CONTRIBUTING.md Full contributing guide (branches, bilingual asset workflow, PR checklist)
DESIGN.md       IBM Carbon design system spec - follow this when building UI
NOTE.md         Original planning note (local only, gitignored)
docs/           Local scratch only, never committed. docs/bob/ holds a Markdown
                mirror of bob.ibm.com/docs for whoever has fetched it
_labs/          English Lab / Recipe assets (a Jekyll collection): _labs/<stack>/<slug>.md
_labs_ko/       Korean mirror of _labs/, same relative paths
_data/          ui.yml - site-chrome translation strings (badge labels, nav)
_layouts/       Jekyll page templates
_includes/      Jekyll partials (e.g. the lab card)
assets/         Site CSS and fonts
ko/             Korean homepage (index.md), served at /ko/
script/preview  Build and serve the site locally
```

Design follows the IBM Carbon Design System (`DESIGN.md`).

Every asset lives at `_labs/<stack>/<slug>.md` plus its mirror `_labs_ko/<stack>/<slug>.md` - e.g. `_labs/spring-boot/explain-repo.md` and `_labs_ko/spring-boot/explain-repo.md`.
See that pair for a complete reference example of the content spec below, fully filled in.
The `_labs/`/`_labs_ko/` underscore prefixes are required (it's how Jekyll recognizes the collections) - the asset's *branch* name still uses the hyphenated `content/<stack>-<slug>` form, that's just a label and doesn't need to match the folders.

---

## Add a lab

Adding an asset is writing **two Markdown files - no code, no HTML, no CSS.** The site renders each new lab, lists it on the homepage, wires it into the filters, and gives every prompt a copy button, all automatically. You never touch a template or a stylesheet.

1. **Copy the skeleton.** [CONTRIBUTING.md → Copy this skeleton](CONTRIBUTING.md#copy-this-skeleton) has two paste-ready blocks. English goes to `_labs/<stack>/<slug>.md`, Korean to `_labs_ko/<stack>/<slug>.md` - same slug, same folder.
2. **Fill the blanks and run it through Bob.** Eight front matter fields, five sections, in both languages. Run each Prompt against a shared [practice repo](CONTRIBUTING.md#practice-repositories) so a reviewer can reproduce your result. Never skip the Bob run - it is the one thing no tool can check for you.
3. **Open a PR.** CI checks the format and that both languages exist; a reviewer checks it against [what makes a lab worth keeping](CONTRIBUTING.md#what-makes-a-lab-worth-keeping). `main` is protected, so everything lands through a PR - once merged, it is live.

First time here? Start with [CONTRIBUTING.md → Start here (day 1)](CONTRIBUTING.md#start-here-day-1) - it walks you through your first lab end to end. The full guide (branch naming, every rule) is in **[CONTRIBUTING.md](CONTRIBUTING.md)**; opening a PR picks up `.github/PULL_REQUEST_TEMPLATE.md` automatically.
