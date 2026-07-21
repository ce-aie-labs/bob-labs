---
title: Generate an Architecture Diagram
lang: en
difficulty: Guided
duration: 10 min
stack: Any
work_replaced: Architecture diagramming
expected_saving: 1 hour → 10 min
---

## Problem

You need to document a codebase's architecture - for onboarding, a design review, or before a big refactor - but hand-drawing UML diagrams is tedious, and they go stale the moment the code changes.

## Prompt

```
Analyze this repository's overall architecture: the main components or
services, how they connect, and what data or calls flow between them.

Generate a Mermaid diagram (a flowchart works well for most repos) that
visualizes this, and save it as docs/architecture/overview.md with a
short description above the diagram code block.
```

## Expected Output

- [ ] A Mermaid diagram (flowchart/graph) identifying the actual components or services in the repo - not generic placeholders like "Service A"
- [ ] Edges labeled with what actually flows between components (an API call, a message, a shared database), not just unlabeled arrows
- [ ] For a multi-language or multi-service repo, each component's role or language called out
- [ ] The diagram saved as a real file (`docs/architecture/overview.md`), with a short description above the Mermaid block, viewable rendered directly on GitHub

Drafted from IBM's own published tutorial ([Generate architecture diagrams](https://bob.ibm.com/docs/ide/tutorials/generate-architecture-diagrams), using the official Galaxium Travels demo repo - a React + Python FastAPI + Java polyglot booking system) rather than independently dry-run tested.

<!-- Bob-verify: unlike the Spring Boot lab, this one hasn't been run at all yet - not through Bob, not through Claude Code. It's drafted directly from Bob's own published tutorial docs (docs/bob/ide/tutorials/generate-architecture-diagrams.md). Needs a real pass through Bob - including confirming the generated Mermaid actually parses/renders - before it's trusted for participants. -->

## Tips

- On a large codebase, ask Bob to scope the diagram to one module or service first ("...just the payment service") - a diagram with too many nodes is more likely to produce Mermaid syntax Bob gets wrong.
- Mermaid's parser is strict. If the diagram doesn't render, paste the parser error back to Bob and ask it to fix that specific line rather than regenerating the whole thing.
- Ask Bob to `/init` the project first (if it hasn't already) - a diagram prompt benefits from Bob already having a map of the codebase.

## Variations

1. **Class diagram**: "Generate a Mermaid `classDiagram` of the core data model - the main entities, their fields, and the relationships between them."
2. **Sequence diagram**: "Generate a Mermaid `sequenceDiagram` tracing what happens end-to-end when a user does `<action>`."
3. **Use case diagram**: "Generate a Mermaid flowchart grouping this system's capabilities by actor - end users, external systems, and any internal services - as a use case diagram."
