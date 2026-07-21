---
title: Explain This Repository
lang: en
difficulty: Guided
duration: 5 min
stack: Java, Spring Boot
work_replaced: Repository onboarding
expected_saving: 30 min → 5 min
---

## Problem

You just cloned an unfamiliar Spring Boot repo and need to get oriented before your first commit - tech stack, package layout, how to run it locally, and where a new feature would actually go.

## Prompt

```
Explain this repository to me. What does it do, what's the tech stack, how is
the code organized (main packages/modules and their responsibilities), how do
I build and run it locally, and where would I go to add a new feature (e.g. a
new REST endpoint or a new entity)?
```

## Expected Output

- [ ] A one-paragraph summary of what the app does, plus the named tech stack (framework version, ORM, template engine, database) - not just "it's a Spring Boot app"
- [ ] Code organization broken down by actual package, each with its responsibility (entities vs. repositories vs. controllers), not just a generic MVC description
- [ ] Concrete build & run commands for whichever build tool(s) the repo has, plus where the app listens once it's running
- [ ] Concrete guidance on where a new entity or endpoint would go, pointing at a specific existing class as the pattern to copy

Validated against [spring-projects/spring-petclinic](https://github.com/spring-projects/spring-petclinic): the response named the actual tech stack (Java 17, Spring Boot, Spring MVC, Spring Data JPA + Hibernate, Thymeleaf, H2/MySQL/Postgres), broke the code down by real package (`model`, `owner`, `vet`, `system`) with the classes in each, gave the real `./mvnw spring-boot:run` / `./gradlew bootRun` commands, and pointed at `VetController`'s existing REST endpoint as the pattern for adding a new one.

<!-- Bob-verify: this Expected Output was validated by running the prompt as an agent (Claude Code) against spring-petclinic, since Bob wasn't reachable from this environment. Give it one real pass through Bob before merging to confirm the response shape matches. -->

## Tips

- If the answer stays at "there's a controller, service, and repository layer" without naming actual classes or packages, it hasn't really read the code - reprompt with "name the actual classes and files, not the pattern."
- Repos with more than one build tool (Maven + Gradle, like petclinic) can get the wrong command suggested - double-check it against whichever wrapper script (`mvnw`/`gradlew`) is actually present.
- On a large monorepo, "explain this repository" alone may only skim the top level. Scope it down: "...explain the `<package>` package specifically" for real depth.

## Variations

1. **Scope to one module**: "Explain just the `<package>` package - what it's responsible for and how it fits into the rest of the app."
2. **Turn it into onboarding docs**: "...and write this up as a short onboarding doc I can drop into CONTRIBUTING.md."
3. **Pair it with a real task**: "...and tell me specifically which files I'd touch to add a `<feature>` endpoint."
