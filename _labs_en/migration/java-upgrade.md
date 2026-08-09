---
title: Automate a Java Version Upgrade
lang: en
category: Migration
difficulty: Guided
duration: 30 min
stack: Java
work_replaced: Manual Java version upgrade
expected_saving: 2 days → 2 hours
---

## Problem

You have a Java 8 or 11 estate to move to a modern LTS (17, 21, or 25), and doing it by hand - dependency bumps, `javax` → `jakarta`, deprecated APIs, then fixing everything that breaks - is slow, risky work. Bob's premium Java Modernization package runs the whole upgrade as a guided, agentic workflow. Note: this is a **paid premium package** - the workflow only runs on accounts that have it enabled.

## Prompt

In **Agent mode**, with a Java project that builds cleanly, run:

```
Run the Java Upgrade workflow on this project.

Requirements:
- Target Java version: 21
- Java distribution: Semeru
- Build tool: Maven (detect pom.xml)
- Detect and migrate all framework-specific code patterns
- Apply OpenRewrite recipes for Java 21 migration
- Run agentic fix cycles until the build passes
- Generate a Mermaid diagram of the workflow steps
- Create a feature branch, commit changes, and open a pull request
```

## Expected Output

- [ ] An upgrade plan that names *your* project's actual frameworks and versions (e.g. Spring Boot 2.7, Hibernate 5.6) and the specific OpenRewrite recipes it will apply - not a generic "upgrade to 21" summary
- [ ] Real code transformations applied and shown as a diff: `javax` → `jakarta`, deprecated-API replacements, `maven-compiler-plugin` source/target bumped to the target version - not just described
- [ ] Agentic fix cycles that iterate Build → Test → Diagnose → Fix until the build and tests actually pass, rather than stopping at the first compilation error for you to fix by hand
- [ ] The workflow's own artifacts: a Mermaid diagram of the steps, plus a feature branch, commit, and PR so the migration is auditable - matching the "compiles on the target version, recipes applied, build/tests pass, PR created" success criteria

Based on IBM Bob's premium [Java Modernization package](https://bob.ibm.com/docs/ide/premium-packages/java-modernization/java-modernization-index) and its published Java Upgrade workflow, which upgrades Java 8/11 to 17, 21, or 25 using OpenRewrite recipes with agentic fix cycles, across Semeru / Temurin / Corretto and both Maven and Gradle.

<!-- Bob-verify: not yet run through Bob, and not runnable in this environment - Java Modernization is a paid premium package we don't have access to. Drafted from the published premium workflow walkthrough (bob-lab-app .../labs/premium-java-modernization). Needs a real pass on the actual premium package before use with participants - in particular whether the single prompt kicks off the workflow (vs the Start Workflow button / `/start-java-mod` skill) and whether the agentic fix cycle runs to a passing build unattended. -->

## Tips

- This is a premium package. The workflow only runs on accounts with Java Modernization enabled - confirm access before demoing it, or the prompt does nothing. It can also be started from the **Start Workflow** button or the `/start-java-mod` skill.
- Start on a small module first to learn the workflow's shape before pointing it at a large estate - the package's own guidance.
- Create a dedicated migration branch first so you can roll back, and let Bob's sub-tasks handle failures - don't force the workflow past a failing build, because the agentic fix cycle is the whole point.
- Review the validation report before deploying anywhere. The workflow gets you a compiling, tested, PR'd upgrade - it doesn't replace a human sign-off on behavior.

## Variations

1. **Liberty replatforming**: "Run the Liberty Replatforming workflow on this project" with an AMA migration bundle - migrate WebSphere to Open/WebSphere Liberty, generate and validate `server.xml`, and deploy with the `liberty-maven-plugin`.
2. **UI modernization**: "Run the UI Modernization workflow" to migrate a legacy JSP/Struts UI to a Spring Boot 3 REST backend and a React + Carbon frontend, component by component, preserving all business rules.
3. **Unit test generation**: "Run the Unit Test Generation workflow" - a strategy-first pass with generate-run-fix loops that iterate to a JaCoCo coverage threshold (e.g. 80% line coverage).
