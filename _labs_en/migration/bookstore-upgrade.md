---
title: Upgrade a Legacy JSF App to Java 21
lang: en
category: Migration
difficulty: Guided
duration: 45 min
stack: Java, JSF
work_replaced: Manual Java 8 to 21 upgrade of a JSF/Servlet app
expected_saving: 1 week → 1 hour
---

## Problem

You have a Java 8 web application - Spring MVC, JSF, `javax.servlet`, packaged as a WAR - and it has to move to Java 21 and Jakarta EE. By hand that is a dependency audit, a `javax` → `jakarta` rename across every file, framework bumps that don't compile until they all move together, and then however many days of fixing whatever broke.

Bob's premium Java Modernization package runs the whole upgrade as a guided, agentic workflow. This lab points it at a real legacy app and then checks the result the way the workflow does not: by asking whether the upgraded app actually starts. Note: this is a **paid premium package** - the workflow only runs on accounts that have it enabled.

## Prompt

### Step 1 - Install Java 21 and Maven

Skip if `java -version` already prints 21+ and `mvn -v` works.

**Windows** - get **Version 21 · Windows · x64** `.msi` from <https://developer.ibm.com/languages/java/semeru-runtimes/downloads/>, enable **Set JAVA_HOME** and **Add to PATH**, open a new terminal. Maven: <https://maven.apache.org/download.cgi>.

**macOS** - `brew install --cask semeru-jdk-open@21 && brew install maven`.

### Step 2 - Get the application

Download **[bob-lab-bookstore.zip](https://github.com/ce-aie-labs/bob-labs/releases/download/lab-assets/bob-lab-bookstore.zip)** and unzip it. Open a terminal in the folder holding `START-HERE.md`.

Inside is **Legacy Bookstore** - Java 8, Spring MVC 4.3, JSF 2.2, Hibernate 5.4, H2 1.4.200, 16 Java files, packaged as a WAR.

### Step 3 - Confirm the start state

```
mvn clean package
```

`BUILD SUCCESS` means you are at the baseline: it compiles on Java 8 target, and it is still `javax`, Spring 4, JSF 2.2. That is what the workflow will move.

### Step 4 - Run the Java Upgrade workflow

Open the `app` folder in Bob. Start the workflow from the **Start Workflow** button, the `/start-java-mod` skill, or Agent mode, and on the **Flow Selection** screen choose **Java Upgrade**. Answer the configuration screen:

```
Java Distribution: Temurin (Eclipse)
Java Version: 21
Jakarta EE Version: Jakarta EE 10
Build tool: Maven

Do not weaken behavior. javax → jakarta and API replacements to make it compile
are fine; do not delete logic. Run the agentic fix cycle until the build passes.
```

Let it run its named stages and fix cycles. Do not force it past a failing build - the fix loop is the point.

### Step 5 - Check what "done" means

When Bob reports success, do not stop. Rebuild yourself:

```
mvn clean package
```

A green build is **not** a running app. This app is a WAR that has to start in a servlet container, and that is a separate fact from compiling - see the Expected Output.

## Expected Output

- [ ] An upgrade that names this app's actual stack and moves it as a set: **Java 8 → 21 (Temurin)**, **Spring 4.3.30 → 6.1.x**, **Hibernate 5.4 → 6.4**, **JSF 2.2 → 4.0.22 (+ Weld CDI)**, **H2 1.4.200 → 2.2.220**, and **log4j 1.2.17 → SLF4J + Logback** - not a generic "upgrade to 21"
- [ ] The **`javax` → `jakarta`** rename applied across the codebase (servlet, faces, persistence) in one pass via OpenRewrite recipes, shown as a diff - not described
- [ ] Named workflow stages and an **agentic fix cycle** that iterates build → diagnose → fix, plus the workflow's own artifacts (a feature branch and commit, a steps diagram)
- [ ] **`mvn clean package` still passes after the upgrade** - the app compiles on Java 21
- [ ] **The catch, and the point of the lab**: a green build did not mean a running app. In the recorded run Bob reported *"Build completed successfully with no errors or warnings"* while the WAR failed to start on Tomcat 10.1 (context startup failed, HTTP 404). Handing Bob the startup **stack trace** got the root cause (a CDI / `@Named` wiring problem) fixed in **one round-trip** - the workflow transforms well but does not verify its own output, so you bring the gate

Seeded from a real run of this premium workflow against this exact fixture (Legacy Bookstore), recorded in the IBM client-engineering Bob-for-Java evaluation. See the premium [Java Modernization package](https://bob.ibm.com/docs/ide/premium-packages/java-modernization/java-modernization-index).

<!-- Bob-verify: not yet run through Bob in this repo, and not runnable here - Java Modernization is a paid premium package we don't have access to. The numbers above are seeded from a real recorded run (VALUE.md: Java 8→21, Spring 4.3.30→6.1.21, JSF 2.2→4.0.22, build-success-but-404-then-fixed-from-the-stacktrace). Run the EN prompt on the real package before use with participants - confirm the Flow Selection wording and that the fix cycle resolves the deploy failure. -->

## Tips

- This is a premium package. The Java Upgrade workflow only runs on accounts with Java Modernization enabled - confirm access before demoing, or the prompt does nothing.
- **Build success is not deploy success.** The whole lesson is that Bob will say the build passed while the container still 404s. Deploy the WAR (or run the package's deploy check) before you call it done, and feed any startup failure back as a stack trace - that is what gets it fixed.
- **Run one workflow at a time.** If you let Java Upgrade auto-chain into the vulnerability fix, results get muddier (see the Vulnerability lab). Finish and verify the upgrade first.
- If Bob answers in generalities, point it at `pom.xml` and the actual `javax` imports and it will go and look.

## Variations

1. **Liberty replatforming**: `Run the Liberty Replatforming workflow` with an AMA migration bundle - move a WebSphere app to Open Liberty, generate and validate `server.xml`, deploy with `liberty-maven-plugin`. Needs a WebSphere-flavored app, not this one.
2. **Then scan it**: after the upgrade lands and starts, run the Vulnerability lab on the result - a modern stack still ships CVEs.
3. **Your own WAR**: run the same workflow on a Java 8/11 service you own. Keep the two rules - don't weaken behavior, verify it starts - because those are what make the result reviewable.
