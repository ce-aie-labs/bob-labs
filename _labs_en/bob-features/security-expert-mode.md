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

Use a working directory where Bob may create `.bob/`, then run all three steps. Review the generated and updated controls before trusting or using the audit results.

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

Build this mode from Skills connected as a pipeline. Keep the rules in a rule file, and create a separate, concrete Skill for evaluating each rule. The pipeline must read each code file and apply the defined rules in sequence. Move to the next file only after every rule has been evaluated. If a file fails any rule, write the result to security-findings-and-remediation.md.

For every finding, keep these three elements together:
{problem location and code + failed rule + improved code}

Run this across the entire codebase. Create all required Skills, the Mode, and the security rules.
```

**Checkpoint:** Confirm that Bob created **Security Expert** (`security-expert`) and inspect every generated file before using it:

```text
.bob/custom_modes.yaml
.bob/rules/security.md
.bob/skills/secret-scan/SKILL.md
.bob/skills/crypto-weakness/SKILL.md
.bob/skills/info-disclosure-check/SKILL.md
.bob/skills/compliance-check/SKILL.md
.bob/skills/security-audit-pipeline/SKILL.md
```

The observed Mode allowed Read but restricted Edit to the report with this tool rule:

```yaml
- - edit
  - fileRegex: "^security-findings-and-remediation\\.md$"
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

**Checkpoint:** Open `security-findings-and-remediation.md`. For every entry, verify the cited file and line, rule applicability, severity, proposed remediation, and whether the relevant file was actually in scope. Treat the report as review input, not a list of confirmed vulnerabilities.

### Step 3 - Update and maintain the Security Expert Mode

When company policy changes, return to **Agent mode** and update the existing Mode:

```
I need to update the Security Expert mode.

Our internal protocol protects passwords, so allow MD5 and SHA-1 when they are used for password protection through that protocol. Instead, add session security rules that require Secure, HttpOnly, and SameSite attributes on session cookies, regenerate the Session ID after a successful login, and invalidate the session on logout or expiration.
```

**Checkpoint:** Review the changes to `.bob/rules/security.md`, `.bob/skills/crypto-weakness/SKILL.md`, and `.bob/skills/compliance-check/SKILL.md`. Confirm that the MD5 and SHA-1 exception applies only to password protection through the approved internal protocol, while other security-sensitive uses remain prohibited. Also confirm that all three session controls are testable and mapped to concrete detection patterns and remediation guidance.

## Expected Output

The observed Korean run created a project-scoped Mode with four rule Skills and one orchestration Skill:

- [ ] SR-01 → `secret-scan`: hardcoded credentials and secrets
- [ ] SR-02 → `crypto-weakness`: MD5 and SHA-1 usage and its context
- [ ] SR-03 → `info-disclosure-check`: sensitive details in client responses
- [ ] SR-04 → `compliance-check`: selected NIST SP 800-53, OWASP ASVS Level 1, and CWE Top 25 controls
- [ ] `security-audit-pipeline`: initialize the report → collect files → apply SR-01 through SR-04 to each file → aggregate the results
- [ ] `security-findings-and-remediation.md`: findings grouped as location and problem code + failed rule + proposed improved code

The Mode update changed three existing files:

- [ ] `.bob/rules/security.md`
  - SEC-02 allows MD5 and SHA-1 for password protection under the internal-protocol exception.
  - SEC-02 instead prohibits the `random` module and MD5 or SHA-1 for non-password security uses such as Session IDs and tokens, and emphasizes classifying the purpose before reporting a finding.
  - SEC-04c requires Secure, HttpOnly, and SameSite cookie attributes, Session ID regeneration after login, and server-side session invalidation on logout or expiration.
  - SEC-04c includes allowed and prohibited Flask examples.
- [ ] `.bob/skills/crypto-weakness/SKILL.md`
  - Added an `EXEMPT` severity for password-related MD5 and SHA-1 under the internal protocol.
  - Split the Pattern Group A and B context filters into `EXEMPT`, `CRITICAL`, and `LOW` classifications.
  - Added `EXEMPT` handling to Step 3 so those cases are not reported.
  - Updated Step 4 to present password changes only as recommendations and added remediation code for non-password security uses.
- [ ] `.bob/skills/compliance-check/SKILL.md`
  - Added missing SameSite to the session-cookie triggers.
  - Added Session ID regeneration checks, including a CWE-384 Session Fixation detection heuristic.
  - Added logout and expiration session-invalidation checks.
  - Added Session Fixation and session-invalidation remediation code to Step 8.
  - Added a Step 9 `sub_issue` field with `cookie_flags`, `session_fixation`, `session_invalidation`, or `jwt_verify`.

Bob's sample audit reported **eight findings**: one hardcoded-configuration result, two response-disclosure results, and five broader results covering missing authentication/authorization, network binding, CORS, container execution, and dependency pinning. It reported zero SR-02 findings. These counts describe Bob's generated report, not independently confirmed vulnerabilities.

The report contained useful leads - for example, it cited `services/booking.py:34`, showed that an error response returned another user's stored name, mapped the issue to SR-03, and proposed a generic response that omitted that name. The missing authentication and ownership checks around booking endpoints also warrant high-priority manual review.

This Mode provides a repeatable LLM- and rule-guided review. It does not prove exploitability or replace SAST, dependency scanning, DAST, threat modeling, or human security review.


## Tips

- Review `.bob/rules/security.md` and every Skill before the first audit. A broad standards name is not a complete, testable control set.
- Define which endpoints require authentication, who may access each resource, and where TLS and network exposure are enforced. Those trust boundaries cannot be inferred reliably from isolated code patterns.
- Keep the review Mode report-only. Put source changes in a separate Plan-mode or approval-gated remediation workflow.
- Require exact evidence for every finding: file, current line, relevant context, rule text, confidence, and validation method.
- Record which tools actually ran. An LLM prediction, regex match, SAST result, dependency advisory, and reproduced exploit are different evidence types.

## Variations

1. **Run one control only**: Invoke `secret-scan`, `crypto-weakness`, or another rule Skill for a focused pre-commit review.
2. **Add stack-specific controls**: Extend the rules with reviewed FastAPI, Spring Security, Express, or container controls while keeping the same report contract.
3. **Verify with scanners**: Follow the Mode with SAST and dependency scans, then annotate each finding as confirmed, rejected, or requiring manual review.
4. **Remediate with approval**: Create a separate Mode that accepts only reviewed report entries, proposes a plan, and waits for approval before editing source files.
