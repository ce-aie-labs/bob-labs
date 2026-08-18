---
title: Find and Fix Dependency Vulnerabilities
lang: en
category: Security
difficulty: Guided
duration: 30 min
stack: Java
work_replaced: Manual CVE triage and dependency remediation
expected_saving: 1 day → 10 min
---

## Problem

An old service pulls in old libraries, and old libraries carry known CVEs - not just the ones you declared, but the transitive ones underneath them. Working out which dependency is actually vulnerable, which version fixes it, and whether the fix breaks anything else is careful, unglamorous work.

Bob's premium Java Vulnerabilities package scans the resolved dependency tree, lifts what it can, and - importantly - **re-scans after fixing to confirm**. This lab runs it on a deliberately vulnerable app. The catch it teaches is procedural: this workflow behaves very differently run on its own versus chained after an upgrade. Note: this is a **paid premium package** - it only runs on accounts that have it enabled.

## Prompt

### Step 1 - Install Java 21 and Maven

Skip if `java -version` prints 21+ and `mvn -v` works. **Windows** - Semeru 21 `.msi` from <https://developer.ibm.com/languages/java/semeru-runtimes/downloads/> (Set JAVA_HOME + Add to PATH), Maven from <https://maven.apache.org/download.cgi>. **macOS** - `brew install --cask semeru-jdk-open@21 && brew install maven`.

### Step 2 - Get the application

Download **[bob-lab-bookstore.zip](https://github.com/ce-aie-labs/bob-labs/releases/download/lab-assets/bob-lab-bookstore.zip)** and unzip it. Open a terminal in the folder holding `START-HERE.md`. Inside is **Legacy Bookstore**, which pins deliberately old, vulnerable libraries - e.g. **H2 1.4.200** (CVE-2022-23221) and **log4j 1.2.17** (CVE-2019-17571).

### Step 3 - Run the Vulnerabilities workflow, on its own

Open the `app` folder in Bob. Start the workflow and choose the **Vulnerabilities** flow. Run it **standalone** - do not let it chain after another workflow (this matters, see Tips):

```
Run the Vulnerability Remediation workflow on this project, standalone.

Scan the resolved dependency tree (transitive included), fix the vulnerable
dependencies with the smallest version change that has a patch, do not change
application behavior, and re-scan to confirm what remains.
```

### Step 4 - Verify with an independent scan

Do not take the summary on trust. Run a scanner that is not Bob against the same tree - for example [`osv-scanner`](https://github.com/google/osv-scanner):

```
osv-scanner --lockfile pom.xml    # or scan the built artifact
```

Compare its findings to what Bob reported fixed and what it says remains.

## Expected Output

- [ ] It scanned the **transitive** tree, not just declared dependencies - a recorded run went from **10 direct dependencies to 37 transitive** scanned and surfaced **43 CVEs**, within ~5% of an independent `osv-scanner` (41)
- [ ] Vulnerabilities lifted with a **small, targeted change** - a recorded standalone run fixed **23 → 0** with a two-line `pom.xml` change, changing no application behavior
- [ ] **Reasoning about the fix version**, not a blind bump - it noted a line had no patch on its current major (e.g. Spring 6.1.x) and chose the minor that does (6.2.x)
- [ ] **It re-scanned after fixing and self-confirmed**: resolved N, remaining 0, newly introduced 0 - and your independent scan in Step 4 agrees
- [ ] Which packages moved, and why - readable enough to take into a review, not just a count that went down

Seeded from real runs of this premium workflow against this fixture and its modernized form (43 CVEs detected; 23 → 0 resolved standalone; re-scan self-confirmation), recorded in the IBM client-engineering Bob-for-Java evaluation. Exact counts depend on the starting state.

<!-- Bob-verify: not yet run through Bob in this repo, and not runnable here - Java Vulnerabilities is a paid premium package we don't have access to. Numbers seeded from real recorded runs (VALUE.md: 10 direct→37 transitive→43 CVEs vs osv-scanner 41; standalone 23→0, two-line pom, re-scan confirms; chained-after-upgrade regressed to 10 newly-introduced with no re-scan). Run the EN prompt on the real package before use with participants; the v1 legacy state's exact CVE count will differ from the post-upgrade figures. -->

## Tips

- This is a premium package - it only runs on accounts with Java Vulnerabilities enabled. Confirm access before demoing.
- **Run it standalone.** The single most important thing here: the vulnerability fix does **not** appear as its own item on the Flow Selection screen, so it is easy to let it auto-chain after Java Upgrade. Chained, a recorded run regressed - 41 → 23 but with **10 newly-introduced** vulnerabilities and **no re-scan**. Run one workflow at a time and re-scan between them.
- **Read which packages moved, not the headline number.** For whoever reviews this, "hsqldb went from X to Y" matters more than "23 fewer findings."
- The re-scan is the gate. If the workflow did not re-scan after fixing, you have a claim, not a result - run your own scan (Step 4).

## Variations

1. **Security first, upgrade later**: when exposure is the urgent part, fix vulnerabilities without touching the Java or framework version, and schedule the upgrade separately.
2. **Benchmark the detector**: run `osv-scanner` first, then the workflow, and see how close the two are on this tree - detection you can trust is the point.
3. **Your own project**: run it standalone on a service you own, then re-scan. Never chain it onto another workflow without a scan in between.
