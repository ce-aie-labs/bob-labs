---
title: Audit a Codebase with a Security Expert Mode
lang: en
category: Bob Features
difficulty: Guided
duration: 25 min
stack: Any
work_replaced: Manually applying security rules across a codebase
expected_saving: 2 hours → 25 min
---

## Problem

A security checklist is useful only when it is applied consistently across the codebase and leaves evidence an engineer can review. Build a project-scoped Custom Mode that separates company security rules into purpose-specific Skills, runs them as a pipeline, and writes a remediation report without changing source code.

### Tutorial overview

In this tutorial, you first inspect Bob's built-in Modes and tool permissions. You then create a project-scoped Security Expert Mode and Skills that apply company security rules, use them to audit a sample project, and generate a vulnerability and remediation report. Finally, you update the existing Mode and Skills for a company policy change and verify that the revised rules are reflected consistently across the audit workflow.

<div class="tutorial-flow" role="list" aria-label="Tutorial steps">
  <div class="tutorial-flow__step" role="listitem">
    <span class="tutorial-flow__number">01</span>
    <strong>Inspect Bob settings</strong>
    <span>Review built-in Modes and automatic approval.</span>
  </div>
  <div class="tutorial-flow__step" role="listitem">
    <span class="tutorial-flow__number">02</span>
    <strong>Build the security environment</strong>
    <span>Create a Custom Mode, Skills, and Rules.</span>
  </div>
  <div class="tutorial-flow__step" role="listitem">
    <span class="tutorial-flow__number">03</span>
    <strong>Audit the project</strong>
    <span>Review the sample code and generated report.</span>
  </div>
  <div class="tutorial-flow__step" role="listitem">
    <span class="tutorial-flow__number">04</span>
    <strong>Apply a policy change</strong>
    <span>Update the Mode and Skills and verify the result.</span>
  </div>
</div>

## Prompt

Run these four steps from a working directory where Bob may create `.bob/`. First inspect Bob's built-in Modes and automatic approval settings. Review every generated or modified control before trusting or using the audit results.

### Step 0 - Get to know Bob

Bob provides three built-in Modes: **Agent**, **Plan**, and **Ask**. Each Mode has a different role, intended use, set of instructions, and tool access. Before starting the hands-on, inspect the built-in Mode definitions and the automatic approval settings.

1. Select the gear icon in the upper-right corner of Bob to open **Bob Settings**.
2. Select the **Modes** tab in the left navigation.
3. Confirm that the built-in **Agent**, **Plan**, and **Ask** Modes are listed.
4. Open each Mode and inspect its **Role definition**, **When to use**, **Custom instructions**, and **Available tools**.

- **Agent**: Use it for implementation work such as editing files and running commands.
- **Plan**: Use it to analyze requirements and design an implementation sequence before changing code.
- **Ask**: Use it to ask questions and receive explanations about code or concepts.

Next, enable automatic approval for the trusted hands-on environment:

1. Select the **Chat** tab in Bob Settings.
2. Under **Toggle auto-approval**, enable each displayed tool.
3. Confirm that automatic approval is enabled for Read, Edit, Execute, MCP, Modes, Subtasks, Subagents, Skills, Todos, and any displayed child tools.

Automatic approval keeps this trusted hands-on moving by allowing Bob to use tools without waiting for confirmation each time. Enable all tools only for this trusted practice project. After the session, disable automatic approval or leave only the permissions needed for your work.

**Checkpoint:** Open the details for Agent, Plan, and Ask. Under **Settings > Chat**, confirm that automatic approval is enabled for every displayed tool and child tool.

A Mode is not only a response style. It limits Bob's **role, operating instructions, and available tools**. Tool configuration determines what Bob can do, while automatic approval determines when Bob must ask before performing an allowed action. Return to least privilege after the hands-on by enabling only the Modes, tools, and permissions required for real work.

### Step 1 - Build a project-scoped security environment

In **Agent mode**, copy the complete prompt below into a new Bob task and run it:

```
I need a Security Expert mode for this project.
This mode must use security audit Skills as a pipeline to audit the code and write a vulnerability and remediation report.
Define the security rules, determine whether every file in the project complies with them, and write vulnerability-and-remediation.md with the code that violated each rule and proposed improved code.

Our company security rules are:
1. Do not use MD5 or SHA-1 for password protection.
2. Comply with NIST SP 800-53, OWASP ASVS Level 1, and the CWE Top 25.

Create the required Skills, Mode, and security rules.
Write every generated Skill and file in English.
```

Wait for Bob to finish. In the Todo list, confirm that Mode design, Skill creation, Custom Mode creation, and final validation are complete.

The generated Modes, Skills, filenames, and file count may differ from this example depending on the Bob version and the result of the run.

The translated draft expects the following project assets based on the verified Korean run:

```text
.bob/custom_modes.yaml
.bob/skills/code-security-scan/SKILL.md
.bob/skills/security-rule-validate/SKILL.md
.bob/skills/vulnerability-report/SKILL.md
```

- `code-security-scan`: Scans the project and collects candidates for security rule violations.
- `security-rule-validate`: Determines whether each candidate violates company rules and the selected security standards.
- `vulnerability-report`: Records the finding, violated rule, severity, evidence, and proposed remediation.
- `custom_modes.yaml`: Defines **Security Expert** (`security-expert`) and connects its role, instructions, tool scope, and Skill execution order.

Verify the responsibilities rather than only the count: complete code scanning, rule validation, and report generation should be separated and connected in the Custom Mode.

This step demonstrates how a detailed description of real work can become reusable project assets. Skills preserve repeatable procedures, while `custom_modes.yaml` combines the role, instructions, tool scope, and Skill sequence into one way of working. Unlike a one-time chat response, the `.bob/` files can be reviewed, versioned, shared, and reused by a team.

For anyone new to these, here is a short summary of the components used in `.bob/`:

- **Mode**: A working environment that defines Bob's role, instructions, and access to tools and Skills. In this lab, Security Expert Mode limits Bob to the work needed for security auditing and report generation.
- **Skill**: A reusable unit that stores the procedure and decision criteria for a repeatable task. A larger workflow can be divided into focused Skills such as code scanning, rule validation, and report generation.
- **MCP server**: A standardized interface through which Bob can connect to external systems or tools. For example, Bob can use allowed MCP tools when it needs information or functionality from an issue tracker, database, or internal API.
- **Rule**: A shared principle or constraint that Bob must follow within the project. Rules can capture coding standards, security policies, or files that must not be modified so that the guidance applies consistently across Modes and Skills.

A Mode defines the role and scope of the overall job, Skills provide the procedures performed within it, and Rules provide requirements that must always be followed. An MCP server is the connection point when the job requires external information or functionality. Not every project needs all four components; this run creates a Mode and Skills.

**Checkpoint:** Confirm that Bob created **Security Expert** (`security-expert`) and inspect every generated file. Verify that the Mode performs code scanning, rule validation, and report generation in order. Edit access must be limited to a report such as `vulnerability-and-remediation.md`; it must not grant permission to modify source code. Select **Security Expert** in the Mode picker before continuing.

### Step 2 - Audit a working sample project

The Custom Mode and Skills created in Step 1 are loaded when a new Bob session starts. Select the `X` at the top of the current Bob chat, close the existing session, and wait for the new task input. Do not continue in the old chat.

Open a new terminal from the same working directory. If the sample branch is not present, run:

```bash
git clone -b bob-learning-path-branch https://github.com/IBM/galaxium-travels
```

When the command finishes, confirm that the `galaxium-travels` directory and its files appear in the Explorer.

Paste this prompt into Bob's new task input and run it:

```
The galaxium-travels project needs a security review.
```

Confirm that Bob switches to the newly loaded **Security Expert** (`security-expert`) Mode and starts the audit. The translated draft expects the Todo list to show these five stages from the Korean run:

1. Collect target files
2. Search patterns by rule
3. Organize detected results
4. Report scan results
5. Write the report

The Todo list exposes how far the stored Skill procedure has progressed. It makes the workflow observable, so a reviewer can see whether a stage was skipped or stopped before report generation.

After all five items finish, open `galaxium-travels/vulnerability-and-remediation.md`. Review the severity summary and each finding's file, line, violated rule, explanation, and proposed remediation.

**Checkpoint:** For every report entry, confirm that the file and line match the current code, the rule applies to that code, the severity and remediation are appropriate, and the file was actually in scope. Treat the report as review input, not a list of confirmed vulnerabilities.

Using a reviewed Security Expert Mode instead of a general Agent lets the audit start with the same role, tool scope, and procedure without repeating those conditions in every prompt. This improves procedural consistency and reduces manual search effort, but it does not guarantee identical wording or findings on every run. A person must inspect the cited code and make the final decision.

### Step 3 - Update and maintain the Security Expert Mode

The existing **Security Expert** Mode has only the permissions needed for security review and report generation. Updating Mode and Skill files requires the edit permission available in **Agent mode**. Close the Security Expert session with the `X`, open a new task, and confirm that **Agent** is selected.

Copy this policy-change prompt into the new Agent task and run it:

```
I need to update the Security Expert mode.

Our approved internal protocol protects passwords, so allow MD5 and SHA-1 only when they are used for password protection through that protocol. Instead, add session security rules that require Secure, HttpOnly, and SameSite attributes on session cookies, regenerate the Session ID after a successful login, and invalidate the session on logout or expiration.
```

Confirm that Bob reads the existing Mode and Skills and identifies every file that must change. The translated draft expects the same four files changed in the Korean run:

```text
.bob/custom_modes.yaml
.bob/skills/code-security-scan/SKILL.md
.bob/skills/security-rule-validate/SKILL.md
.bob/skills/vulnerability-report/SKILL.md
```

**Checkpoint:** Review the diff for all four files. Confirm that the MD5 and SHA-1 exception applies only to password protection through the approved internal protocol. Confirm that Secure, HttpOnly, and SameSite cookie attributes, Session ID regeneration after login, and session invalidation on logout or expiration map to concrete detection patterns, validation criteria, and remediation code. Also verify that rule meaning, Skill order, report format, and report-only tool scope did not drift.

This step demonstrates how Bob can translate a natural-language policy change into coordinated updates across a Mode and its Skills. It reduces manual file-by-file maintenance, but the prompt does not prove the changes are correct. Inspect the actual diff before accepting it.

## Expected Output

<!-- Bob-verify: This English draft is based on the verified Korean run and has not yet been run through Bob in English. Replace the expected filenames, Todo labels, report name, findings, and screenshots with the observed English run before participant use. -->

The English run should produce a project-scoped Security Expert Mode with three distinct responsibilities:

- [ ] `.bob/custom_modes.yaml`: Defines the role, instructions, allowed tools, and Skill execution sequence.
- [ ] `.bob/skills/code-security-scan/SKILL.md`: Collects audit targets and finds candidates for security violations.
- [ ] `.bob/skills/security-rule-validate/SKILL.md`: Validates candidates against company rules and selected security standards.
- [ ] `.bob/skills/vulnerability-report/SKILL.md`: Records the file, line, violated rule, explanation, severity, and proposed remediation.
- [ ] `galaxium-travels/vulnerability-and-remediation.md`: Keeps the problem location and code, applicable rule, and proposed improved code together.

The policy update should coordinate changes across the same four Mode and Skill files:

- [ ] The MD5 and SHA-1 exception is limited to password protection through the approved internal protocol.
- [ ] Detection and validation cover Secure, HttpOnly, and SameSite session cookie attributes.
- [ ] Detection and validation cover Session ID regeneration after successful login.
- [ ] Detection and validation cover session invalidation on logout or expiration.
- [ ] The report format and report-only source-code boundary remain unchanged.

The verified Korean sample audit produced eight findings and useful investigation leads, including a response in `services/booking.py:34` that returned another user's stored name. That count and evidence must not be presented as the English result until reproduced in the English run. Generated findings are not independently confirmed vulnerabilities.

This Mode repeatedly applies a reviewed role, tool scope, rules, and workflow. It does not make every run identical, prove exploitability, or replace SAST, dependency scanning, DAST, threat modeling, exploit verification, or human security review.

## Tips

- Review `.bob/custom_modes.yaml` and every Skill before the first audit. A broad standards name is not a complete, testable control set.
- Define which endpoints require authentication, who may access each resource, and where TLS and network exposure are enforced. Isolated code patterns cannot reliably infer those trust boundaries.
- Keep the review Mode report-only. Put source changes in a separate Plan-mode or approval-gated remediation workflow.
- Require exact evidence for every finding: file, current line, relevant context, rule text, confidence, and validation method.
- Record which tools actually ran. An LLM judgment, regex match, SAST result, dependency advisory, and reproduced exploit are different evidence types.
- Version `.bob/` with the code so the team can share and reuse reviewed Modes and Skills as working assets.

## Variations

1. **Run one procedure only**: Invoke `code-security-scan` or `security-rule-validate` for a focused pre-commit review.
2. **Add stack-specific controls**: Add reviewed FastAPI, Spring Security, Express, or container controls while keeping the same report contract.
3. **Verify with scanners**: Follow the Mode with SAST and dependency scans, then mark each finding as confirmed, rejected, or requiring manual review.
4. **Remediate with approval**: Create a separate Mode that accepts only reviewed report entries, proposes a plan, and waits for approval before editing source files.
5. **Combine external context and specialized work**: Connect Skills to MCP servers for issue trackers, internal documents, or database context, or divide security, testing, and documentation work among Subagents. Allow only the required Skills, MCP servers, Tools, Subagents, and minimum permissions in the Custom Mode.

**Repetitive work to Bob, final review by people.**
