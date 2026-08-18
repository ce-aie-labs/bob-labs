---
title: Generate Tests for Untestable Legacy Code
lang: en
category: Test Generation
difficulty: Guided
duration: 30 min
stack: Java
work_replaced: Writing a unit test suite for untested legacy code by hand
expected_saving: 3 days → 15 min
---

## Problem

The legacy service has zero tests, and it is not test-friendly: dependencies are `new`-ed in field initializers, repositories hold a `static` shared map. You cannot write a good test until you can inject a fake, and adding those seams by hand across a codebase is the slow, fiddly part everyone puts off.

Bob's premium Java Unit Testing package generates a real suite - it finds the seams it needs, adds them without changing runtime behavior, and iterates generate-run-fix to a coverage target. This lab runs it on a real untested app and checks the suite actually passes. Note: this is a **paid premium package** - it only runs on accounts that have it enabled.

## Prompt

### Step 1 - Install Java 21 and Maven

Skip if `java -version` prints 21+ and `mvn -v` works. **Windows** - Semeru 21 `.msi` from <https://developer.ibm.com/languages/java/semeru-runtimes/downloads/> (enable Set JAVA_HOME + Add to PATH), Maven from <https://maven.apache.org/download.cgi>. **macOS** - `brew install --cask semeru-jdk-open@21 && brew install maven`.

### Step 2 - Get the application

Download **[bob-lab-bookstore.zip](https://github.com/ce-aie-labs/bob-labs/releases/download/lab-assets/bob-lab-bookstore.zip)** and unzip it. Open a terminal in the folder holding `START-HERE.md`. Inside is **Legacy Bookstore** - Java 8, Spring MVC + JSF, 16 Java files, and no tests.

### Step 3 - Confirm there are no tests

```
mvn test
```

It builds and runs, and reports **no tests** - that is the starting condition.

### Step 4 - Run the Unit Test Generation workflow

Open the `app` folder in Bob. Start the workflow (**Start Workflow** button / `/start-java-mod` / Agent mode) and choose **Unit Test Generation** on the Flow Selection screen:

```
Run the Unit Test Generation workflow on this project.

Target: JaCoCo line coverage 80%+
Do not change production behavior. If a class is not testable, add a test seam
(e.g. a package-private constructor for injection) rather than rewriting logic.
Iterate generate-run-fix until the tests pass.
```

### Step 5 - Check the suite

```
mvn test
```

The tests Bob wrote should run and pass. Open the JaCoCo report it generates (`target/site/jacoco/index.html`) for coverage.

## Expected Output

- [ ] **0 → a full passing suite** (a recorded run produced **132 tests, all passing**), not a handful of happy-path stubs
- [ ] **Coverage measured, not claimed**: a JaCoCo report - the recorded run reached **91% instruction / 89% branch**
- [ ] **The untestable seam handled correctly** - the hardest part of legacy testing. Where a bean created its dependency in a field (`this.bookService = new BookServiceImpl();`), Bob added an **injection constructor** for tests while leaving the production constructor and behavior exactly as-is:
  ```java
  public BookBean() { this.bookService = new BookServiceImpl(); }  // production, unchanged
  BookBean(BookService bookService) { this.bookService = bookService; }  // for tests
  ```
- [ ] Meaningful assertions on the real logic (stock decrement, order totals, validation in `OrderServiceImpl.placeOrder`) - not `assertNotNull` filler
- [ ] It ran generate-run-fix itself: tests that failed on the first pass were fixed by Bob, not handed back to you

Seeded from a real run of this premium workflow against this exact fixture (Legacy Bookstore) - 0 → 132 tests, 91% / 89% coverage, 5.42 credits - recorded in the IBM client-engineering Bob-for-Java evaluation.

<!-- Bob-verify: not yet run through Bob in this repo, and not runnable here - Java Unit Testing is a paid premium package we don't have access to. Numbers seeded from a real recorded run (VALUE.md: 0→132 tests all green, 91% instruction / 89% branch, the injection-constructor seam on BookBean). Run the EN prompt on the real package before use with participants, and confirm the exact coverage lands. -->

## Tips

- This is a premium package - it only runs on accounts with Java Unit Testing enabled. Confirm access before demoing.
- **A test suite is an asset, not a gate.** These tests are excellent to have, but they do not start a servlet container - in the recorded evaluation the 0-test and the 132-test versions of the app *failed deployment identically*. Use the suite to catch regressions; use a deploy check to catch "does it run" (see the Upgrade lab).
- The value is in the **seams**, not the count. Read how Bob made an untestable class testable without touching its behavior - that is the part a person would have agonized over.
- Run this **before** an upgrade when you can: a suite you trust turns the upgrade from a leap into a checked step.

## Variations

1. **Safety net first**: generate tests on a legacy service *before* modernizing it, so the upgrade has something to verify against.
2. **Raise the bar**: re-run with `line coverage 90%+` and compare what new tests it writes for the uncovered branches.
3. **Your own module**: point it at one untested module you own - a strategy pass first, then generate-run-fix - rather than a whole estate at once.
