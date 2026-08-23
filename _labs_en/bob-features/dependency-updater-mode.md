---
title: Modernize Multiple Projects with a Local Dependency Updater Mode
lang: en
category: Bob Features
difficulty: Guided
duration: 20 min
stack: Any
work_replaced: Manually setting up dependency inventory, research, approval, and verification workflows for each project
expected_saving: 1 day → 20 min
---

## Problem

Safely modernizing a project's libraries requires more than changing every version to `latest`. Each project needs the actual resolved versions of its direct and transitive dependencies, a reproducible baseline, current official compatibility evidence, a human-approved change scope, and post-update verification using the same checks.

In this example, first create a Dependency Updater Mode in the local `.bob/` environment of a dedicated dependency-modernization workspace. Then clone target projects beneath this workspace and modernize them one at a time with the same Mode. The user's global `~/.bob/` configuration remains unchanged.

## Prompt

Use a disposable clone or a clean branch because an approved run may modify manifests, lockfiles, and source code.

### Step 1 - Create the local Mode first

In **Agent mode**, ask Bob to create the Mode and Skills inside this workspace:

```
Create a project-scoped Custom Mode named Dependency Updater for use only in the current workspace. After this Mode is ready, I will clone target projects into subdirectories and use it with them.

Install the Mode and every Skill it uses only under this workspace's .bob directory. Create .bob/custom_modes.yaml and project Skills under .bob/skills/. Do not create or modify any Mode or Skill under ~/.bob/. The Mode must be available only while this workspace is open, and each run must accept a target project directory.

The purpose of this Mode is to identify the direct and transitive dependencies used by the selected target project, investigate which libraries need updates, analyze compatibility impacts using official documentation and release notes, and modernize only the scope approved by a person.

Use the following rules as the foundation.

1. Read the current and target versions directly from the selected target project's manifests and lockfiles.
2. Do not select a version merely because it is "latest". Base the choice on the target version, release date, support status, and compatibility with the current runtime.
3. For public libraries, prioritize official documentation, official release notes, migration guides, and package-registry information.
4. Do not conclude that a breaking change exists or that versions are compatible based only on blogs, search summaries, or the LLM's existing knowledge.
5. For internal libraries, use only material Bob can actually access, such as documentation within the target project, internal package metadata, and changelogs. Never include internal code or package information in an external search.
6. Before any update, run the target project's existing build and tests and record the baseline.
7. Investigate deprecated APIs, removed APIs, configuration changes, runtime version requirements, peer dependencies, transitive dependencies, and lockfile changes.
8. Do not modify source code, manifests, or lockfiles before a person approves the change plan.
9. Store each target project's dependency inventory, baseline, research results, change plan, command output, and report inside that target project directory. Do not mix evidence from different targets.

Build the pipeline in this order.
Dependency inventory -> current-state tests -> official documentation and release-note research -> breaking-change and deprecated-API analysis -> change plan -> human approval -> dependency and code changes -> tests, build, and verification -> final report

Create the individual workspace Skills required by these rules and this pipeline. Configure the local Mode to orchestrate those Skills in order for the target directory supplied when it runs. Do not overwrite unrelated workspace Modes or Skills that already exist.
```

**Checkpoint:** Bob should create **Dependency Updater** (`dep-updater`) and eight Skills at these paths:

```text
.bob/custom_modes.yaml
.bob/skills/dep-scan/SKILL.md
.bob/skills/dep-baseline/SKILL.md
.bob/skills/dep-research/SKILL.md
.bob/skills/dep-breaking-change/SKILL.md
.bob/skills/dep-plan/SKILL.md
.bob/skills/dep-apply/SKILL.md
.bob/skills/dep-verify/SKILL.md
.bob/skills/dep-report/SKILL.md
```

Confirm that no file was created or changed under `~/.bob/`. The Skills should run in the listed order, accept a target project directory, exclude internal package names and metadata from public queries, and repeat the baseline commands during verification.

Also inspect the generated tool groups. The observed prototype allowed unrestricted `edit` and `execute`, so the pre-approval stop was an **instruction-based boundary**, not a boundary enforced through tool file restrictions. Confirm that Bob actually stops, and review every requested edit and command.

Before continuing, select the workspace-local **Dependency Updater** in the Mode picker.

### Step 2 - Clone and modernize the first project

After the Mode is ready, clone the observed example beneath the current workspace:

```bash
git clone -b bob-learning-path-branch https://github.com/IBM/galaxium-travels
```

Keep the current workspace open and run this prompt:

```
Modernize the dependencies in the ./galaxium-travels project.
```

Bob should run `dep-scan` through `dep-plan` for that target directory and then stop. Review every proposed current and target version, primary-source URL, compatibility conclusion, manifest or source change, lockfile command, and vulnerability-remediation command.

It is best to approve only a small package subset at first. After `dep-apply`, have Bob run `dep-verify` using exactly the same commands as the baseline, then generate the report with `dep-report`.

**Checkpoint:** Inspect the manifest and source diff, the complete lockfile diff, before-and-after build, test, and lint results, scanner or advisory evidence, per-stage timings, and the final report. A completion message, registry response, or elapsed-time value alone does not prove compatibility or security.

### Step 3 - Clone and modernize another project

After the first run, clone another project into a separate subdirectory of the same workspace. Do not recreate or copy the Mode. Keep **Dependency Updater** selected and explicitly identify the second target in the prompt:

```text
Modernize the dependencies in the ./another-project project.
```

The same workspace-local Mode should process one target directory at a time while keeping each target's dependency inventory, baseline, evidence, approval, diffs, timings, and report separate. If a project requires substantially different dependency rules, create another modernization workspace with its own `.bob/` definitions instead of changing the shared workflow during a run.

## Expected Output

Before any target project is cloned, the modernization workspace should contain one local Mode and eight local Skills under `.bob/`. Each cloned target should keep its dependency inventory, baseline, approval record, verification comparison, and report inside its own project directory. The same Mode should process multiple sibling projects one at a time without changing the global `~/.bob/` configuration.

The observed Korean run created the project-scoped pipeline, used it with `galaxium-travels`, and recorded this baseline:

- Build: passed
- Tests: 29 passed, 0 failed
- Lint: already failing with 9 errors and 1 warning
- npm audit: 3 HIGH findings
- Dependency scope: 24 direct npm dependencies, 10 direct Python dependencies, and 326 resolved npm transitive dependencies

After the modernization was approved, the report recorded changes to 18 frontend version constraints, conversion of 10 previously unversioned Python requirements to exact pins, execution of `npm audit fix`, and an npm lockfile change. No source file changed in the approved batch. TypeScript, Tailwind CSS 4, Vite 8, and `lucide-react` were classified as blocked or deferred instead of being applied.

After the changes, the build passed, all 29 tests continued to pass, and npm audit reported zero findings.

<!-- Bob-verify: the Mode, eight Skills, pipeline, and the baseline and report figures above reflect the observed Korean run. Run the English prompts independently in Bob before participant use and update this section if the generated components or results differ. -->

## Tips

- Review `.bob/custom_modes.yaml` and every generated `SKILL.md` before cloning a target project or selecting the Mode. Generated orchestration should receive the same scrutiny as a script.
- Keep the Mode in the modernization workspace and each target's package data and report in that target directory. Do not copy versions or evidence from one project into another.
- Add the workspace `.bob/` directory to version control only when the team intends to share and review the modernization workflow. Otherwise, keep it local.
- Approve one ecosystem or a small package subset first. Smaller batches make the causes of lockfile changes and regressions easier to trace.
- Preserve the exact baseline commands and execution environment. Changing test flags or runtimes between the baseline and verification invalidates the comparison.
- Never send internal package names, versions, source fragments, or private-registry metadata to a public registry or search service. Use only organization-approved internal sources.

## Variations

1. **Different Mode for each workspace**: Add stricter lint and browser-test stages to a frontend modernization workspace, and database-migration checks to a backend workspace.
2. **Patch and security fixes only**: Keep each package within its current major version and approve only updates connected to reviewed advisories.
3. **Internal registry workflow**: Replace public research commands for internal dependencies with organization-approved registry metadata and changelog queries while preserving the no-exfiltration boundary.
