---
title: Design a Pseudo Project with a Custom Mode
lang: en
category: Bob Features
difficulty: Guided
duration: 20 min
stack: Any
work_replaced: Manually assembling a technical design package
expected_saving: 2 hours → 20 min
---

## Problem

You want to explore a technology idea far enough for an engineering team to evaluate it, but not spend time provisioning infrastructure or writing runnable code yet. A project-scoped Custom Mode can connect reusable Skills into one workflow that researches the idea, plans it, writes implementation-level pseudo code, and packages the architecture for review.

## Prompt

Run both steps in a project where Bob may create a local `.bob/` directory and design documents. Review each checkpoint before continuing.

### Step 1 - Build the project-scoped environment

In **Agent mode**, ask Bob to create and install the Mode and its Skills:

```
I want to combine emerging technologies with new ideas to create pseudo projects. Create a mode that automates this, configure the skills it needs, and install them so they are available to Bob in this project's conversations.

Structure it as:
Technology web search -> project planning -> pseudo code and architecture.md -> architecture visualization

The goal is to produce implementation-level pseudo code, an architecture explanation and visualization, and a README for each idea.
```

**Checkpoint:** Confirm that Bob created a project-scoped **Pseudo Project** Mode in `.bob/custom_modes.yaml` and four Skill definitions under `.bob/skills/`: `web-tech-search`, `pseudo-project-plan`, `pseudo-code-gen`, and `architecture-viz`. Read those files before approving their use. The Mode should show progress with `update_todo_list`, carry the project metadata between stages, and chain the four Skills automatically after the required domain and idea decisions.

Select **Pseudo Project** in the Mode picker before the next step.

### Step 2 - Use the environment for a GraphRAG idea

In the new Mode, run:

```
I want to try GraphRAG. Find a graph methodology that has not yet been applied to it and explore how it could be incorporated.

Do not provision or implement the runtime environment. Build only the methodology design and implementation-level pseudo code.
```

**Checkpoint:** Open the generated plan, architecture, pseudo-code files, and README. Confirm that they form a consistent design package and that Bob did not provision infrastructure or present pseudo code as a runnable implementation.

## Expected Output

The observed Korean run installed **Pseudo Project** (`pseudo-project`) for the current project:

- [ ] `.bob/custom_modes.yaml`
- [ ] `.bob/skills/web-tech-search/SKILL.md`
- [ ] `.bob/skills/pseudo-project-plan/SKILL.md`
- [ ] `.bob/skills/pseudo-code-gen/SKILL.md`
- [ ] `.bob/skills/architecture-viz/SKILL.md`
- [ ] A four-stage pipeline—technology research → project planning → pseudo code and architecture → visualization and README—with progress shown through `update_todo_list`

Using the GraphRAG prompt produced a design named **TemporalHeteroGraphRAG** with this manifest:

```text
docs/project-plan.md
docs/architecture.md
docs/pseudo/01-domain-models.pseudo.md
docs/pseudo/02-temporal-kg-builder.pseudo.md
docs/pseudo/03-hgt-encoder.pseudo.md
docs/pseudo/04-temporal-retriever.pseudo.md
docs/pseudo/05-community-summarizer.pseudo.md
docs/pseudo/06-causal-path-extractor.pseudo.md
docs/pseudo/07-llm-answer-generator.pseudo.md
docs/pseudo/08-pipeline-orchestrator.pseudo.md
README.md
[HTML summary artifact]
```

The package proposed a temporal knowledge graph, heterogeneous graph transformer encoding, temporal-aware retrieval, Leiden community summarization, causal-path extraction, grounded answer generation, and pipeline orchestration. `docs/architecture.md` and `README.md` included Mermaid system, data-flow, and sequence diagrams.

These are **design and pseudo-code artifacts**. The run did not create a working environment, execute the pipeline, or benchmark it. Its statements that the methods are unused or outperform baseline GraphRAG are hypotheses to review, not verified results. The generated research Skill's helper used curated 2024–2025 fallback data, so add current primary-source citations before retaining novelty comparisons and require an implementation plus benchmark before making performance claims.

<!-- Bob-verify: the Custom Mode, project-scoped paths, Skill chain, and GraphRAG artifacts above reflect the observed Korean run. Run both English prompts independently in Bob before participant use and update this section if the generated components, paths, or outputs differ. -->

## Tips

- Keep the Mode and Skills in the participant project's `.bob/` directory. Unlike a global `~/.bob/` installation, this makes the workflow project-scoped and reviewable with the project.
- Treat generated research as a lead, not evidence. Open cited primary sources and check publication dates before calling a method new or absent from existing implementations.
- Keep architecture claims separate from measurements. Words such as "faster," "more accurate," or "secure" need executable code, a test setup, and observed results.
- Review the complete artifact manifest, not only Bob's completion message. Module names, interfaces, diagrams, and the plan should agree.
- The generated `docs/` files belong in the participant's working project; do not copy another run's design package into this lab repository.

## Variations

1. **Explore another technology**: Replace GraphRAG with an event-streaming, observability, or local-model idea while preserving the four-stage Mode.
2. **Stop at architecture**: Disable automatic handoff to `architecture-viz` when only a reviewed plan, module contracts, and pseudo code are needed.
3. **Change the final deliverable**: Replace `architecture-viz` with a Skill that turns the approved design into ADRs, an implementation backlog, or review tickets.
