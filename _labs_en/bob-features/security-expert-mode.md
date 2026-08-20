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

A security checklist is useful only when it is applied consistently and produces evidence an engineer can review. Build a project-scoped Custom Mode that turns company rules into focused Skills, runs them as a codebase-wide pipeline, and writes a remediation report without changing source code.

## Prompt

Use a working directory where Bob may create `.bob/`, then run both steps. Review the generated controls before trusting or using the audit results.

### Step 1 - Build the project-scoped security environment

In **Agent mode**, ask Bob to create the Mode, rules, and Skills:

```
I need a Security Expert mode for this project. First create the project's security rules. The mode must determine whether every file in the project complies with those rules, identify the exact file and code for each problem, state which rule it violates, and propose improved code.

Our company security rules are:
1. Do not hardcode secrets such as passwords, API keys, tokens, or certificates in code or configuration files.
2. Do not use MD5 or SHA-1 for password protection.
3. Do not expose stack traces, database information, internal paths, or implementation details in client responses.
4. Comply with NIST SP 800-53, OWASP ASVS Level 1, and the CWE Top 25.
Add any other minimum security policies that are necessary.

Build this mode from Skills connected as a pipeline. Keep the rules in a rule file, and create a separate, concrete Skill for evaluating each rule. The pipeline must read each code file and apply the defined rules in sequence. Move to the next file only after every rule has been evaluated. If a file fails any rule, write the result to 취약점_및_개선코드.md.

For every finding, keep these three elements together:
{problem location and code + failed rule + improved code}

Run this across the entire codebase. Create all required Skills, the Mode, and the security rules.
```

**Checkpoint:** Confirm that Bob created **Security Expert** (`security-expert`) and inspect every generated file before using it:

```text
.bob/custom_modes.yaml
.bob/rules/security-rules.md
.bob/skills/secret-scan/SKILL.md
.bob/skills/crypto-check/SKILL.md
.bob/skills/info-disclosure-check/SKILL.md
.bob/skills/compliance-check/SKILL.md
.bob/skills/security-audit-pipeline/SKILL.md
```

The observed Mode allowed Read but restricted Edit to the report with this tool rule:

```yaml
- - edit
  - fileRegex: "^취약점_및_개선코드\\.md$"
```

Confirm that there is no broader Edit permission. Select **Security Expert** in the Mode picker before continuing.

### Step 2 - Audit a working sample project

From the same working directory, clone the sample branch if it is not already present:

```bash
git clone -b bob-learning-path-branch https://github.com/IBM/galaxium-travels
```

In **Security Expert** mode, run:

```
The galaxium-travels project needs a security review.
```

**Checkpoint:** Open `취약점_및_개선코드.md`. For every entry, verify the cited file and line, rule applicability, severity, proposed remediation, and whether the relevant file was actually in scope. Treat the report as review input, not a list of confirmed vulnerabilities.

## Expected Output

The observed Korean run created a project-scoped Mode with four rule Skills and one orchestration Skill:

- [ ] SR-01 → `secret-scan`: hardcoded credentials and secrets
- [ ] SR-02 → `crypto-check`: MD5 and SHA-1 usage and its context
- [ ] SR-03 → `info-disclosure-check`: sensitive details in client responses
- [ ] SR-04 → `compliance-check`: selected NIST SP 800-53, OWASP ASVS Level 1, and CWE Top 25 controls
- [ ] `security-audit-pipeline`: initialize the report → collect files → apply SR-01 through SR-04 to each file → aggregate the results
- [ ] `취약점_및_개선코드.md`: findings grouped as location and problem code + failed rule + proposed improved code

Bob's sample audit reported **eight findings**: one hardcoded-configuration result, two response-disclosure results, and five broader results covering missing authentication/authorization, network binding, CORS, container execution, and dependency pinning. It reported zero SR-02 findings. These counts describe Bob's generated report, not independently confirmed vulnerabilities.

The report contained useful leads—for example, it cited `services/booking.py:34`, showed that an error response returned another user's stored name, mapped the issue to SR-03, and proposed a generic response that omitted that name. The missing authentication and ownership checks around booking endpoints also warrant high-priority manual review.

The same report demonstrates why the checkpoint matters:

- `sqlite:///./booking.db` contains no credential and is likely a configuration-portability concern, not a hardcoded secret.
- Binding to `0.0.0.0` is often required inside a container; exposure depends on ingress, firewall, authentication, and deployment context.
- Wildcard CORS with credentials needs framework and browser-behavior validation before its exploitability is described.
- Pinning dependencies improves reproducibility but does not prove that versions are vulnerability-free. Verify suggested versions with current advisories and a dependency scanner.
- Requiring a Red Hat base image was not one of the supplied company rules. Keep only organization-approved policy; review non-root container execution separately.
- The rules classify authentication/authorization bypass as High, while the report labeled complete authentication absence Medium. Reconcile severity before prioritizing work.
- Eight findings appeared across six distinct paths, while the report stated eight files with violations. Count findings and affected files separately.
- The pipeline's general glob list did not include `Dockerfile` or `requirements.txt`, although both appeared in the report. Review collection logic and exclusions before accepting an “entire codebase” claim.

This Mode provides a repeatable LLM- and rule-guided review. It does not prove exploitability or replace SAST, dependency scanning, DAST, threat modeling, or human security review.

<!-- Bob-verify: the Mode, project-scoped files, pipeline, and eight-result report above reflect the observed Korean run. Run both English prompts independently in Bob before participant use and update this section if generated components or results differ. -->

## Tips

- Review `.bob/rules/security-rules.md` and every Skill before the first audit. A broad standards name is not a complete, testable control set.
- Define which endpoints require authentication, who may access each resource, and where TLS and network exposure are enforced. Those trust boundaries cannot be inferred reliably from isolated code patterns.
- Keep the review Mode report-only. Put source changes in a separate Plan-mode or approval-gated remediation workflow.
- Require exact evidence for every finding: file, current line, relevant context, rule text, confidence, and validation method.
- Record which tools actually ran. An LLM prediction, regex match, SAST result, dependency advisory, and reproduced exploit are different evidence types.

## Variations

1. **Run one control only**: Invoke `secret-scan`, `crypto-check`, or another rule Skill for a focused pre-commit review.
2. **Add stack-specific controls**: Extend the rules with reviewed FastAPI, Spring Security, Express, or container controls while keeping the same report contract.
3. **Verify with scanners**: Follow the Mode with SAST and dependency scans, then annotate each finding as confirmed, rejected, or requiring manual review.
4. **Remediate with approval**: Create a separate Mode that accepts only reviewed report entries, proposes a plan, and waits for approval before editing source files.
