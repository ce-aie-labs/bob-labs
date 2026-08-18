---
title: Size Up a Legacy Java Application
lang: en
category: Migration
difficulty: Guided
duration: 30 min
stack: Java, Spring Boot
work_replaced: Pre-upgrade assessment of a legacy service
expected_saving: 2 days → 30 min
---

## Problem

Somebody hands you a Java service nobody has opened in years. Before an upgrade can even be scheduled, someone has to work out what it is built on, what has aged out, what is exposed, and what has to move before what. That reading job takes a day or two, and it is usually the reason the upgrade keeps getting postponed.

This lab gives you exactly such a service and gets you to that answer in half an hour - installing Java and downloading the application included. With both already in place it is fifteen minutes. Nothing is modified, so it is safe to repeat, and it needs no add-on packages.

## Prompt

### Step 1 - Install Java 21

Skip this if `java -version` already prints 21 or higher.

**Windows**

1. Open <https://developer.ibm.com/languages/java/semeru-runtimes/downloads/>
2. Choose **Version 21**, **Windows**, **x64**, and download the **`.msi`** installer.
3. Run it. On the options screen turn on **Set JAVA_HOME variable** and **Add to PATH** - they are not always on by default.
4. Close every terminal window and open a **new** PowerShell. An old window keeps the old PATH and will tell you Java is missing when it isn't.

**macOS**

```
brew install --cask semeru-jdk-open@21
```

No Homebrew? Download the `.pkg` from the same page - **aarch64** for Apple Silicon, **x64** for Intel - and run it.

Then check, on either system:

```
java -version
```

You want the first line to say `21`. Anything lower and the rest of the lab will not work.

### Step 2 - Get the application

Download **[bob-lab-petclinic.zip](https://github.com/ce-aie-labs/bob-labs/releases/latest/download/bob-lab-petclinic.zip)** (39 MB) and unzip it. Open a terminal in the unzipped folder - the one holding `START-HERE.md`.

Inside is Spring PetClinic REST, a veterinary clinic API from 2019: 99 Java files, 169 tests, Java 8, Spring Boot 2.1.5.

### Step 3 - Watch it work

It is important to see this before touching anything. The application is old, not broken.

```
java -jar petclinic-legacy.jar
```

It takes about five seconds. Then open this in a browser:

```
http://localhost:9966/petclinic/swagger-ui.html
```

That is the API console the application ships with. Everything this service offers is listed - `GET /vets`, `POST /owners`, `GET /pets` and the rest. Expand any of them, press **Try it out** and then **Execute**, and a real response comes back with six vets in it, by name.

Leave the application running - you come back to this address at the end of the next lab.

### Step 4 - See where it stands

Leave that running and open a **second terminal** in the same folder:

```
java check.java
```

It builds the project on Java 21, reads the compiled bytecode, and reports back. The first run downloads libraries and takes a few minutes. Everything it prints was obtained by running something, not by anyone claiming it.

### Step 5 - Ask Bob

Open the `app` folder in Bob and run this in **Agent mode**:

```
I need to move this repository to Java 21 and Spring Boot 3.x.

First, tell me where it stands today:
- what it is built on - Java version, build tool, frameworks and their versions
- what will break on Java 21 - javax to jakarta, removed APIs, build plugins
  that cannot run there
- which of the libraries it depends on have known vulnerabilities, with the
  version each one is pinned to

Then give me the order to do it in. One jump or an intermediate version, and
why that order.

Do not change any code yet.
```

## Expected Output

- [ ] The real stack with versions - Java 1.8, Spring Boot 2.1.5.RELEASE, Hibernate 5.3, Spring Security 5.1, Springfox Swagger2 2.6.1. Noticing that `pom.xml` never declares `java.version` and inherits the Boot BOM default is the sign it actually read the file
- [ ] `javax` → `jakarta` given as **files and line numbers** - `BaseEntity.java:18` (persistence), `Owner.java:30` (validation), `OwnerRestController.java:21` (transaction), `Vets.java:21` (xml.bind). Locations, not a list of package names
- [ ] A warning that **`javax.sql.DataSource` must be left alone** - it is JDK standard, and a blanket rename breaks seven JDBC repositories. This is the line that separates understanding from find-and-replace
- [ ] What Spring Boot 3 removed - `WebSecurityConfigurerAdapter` → `SecurityFilterChain`, `@EnableGlobalMethodSecurity` → `@EnableMethodSecurity`, Springfox → springdoc-openapi
- [ ] **jacoco `0.8.2` named as unable to handle Java 11+** - that is the real cause of the build failure from Step 4, answered without being asked
- [ ] Vulnerable libraries by name, pinned version and CVE - `hsqldb 2.4.1` (CVE-2022-41853, RCE), `jackson-databind 2.9.x`, `mysql-connector-java 8.0.16`, and the Spring Boot BOM itself (Spring4Shell, CVE-2022-22965)
- [ ] The staged route 2.1 → 2.7 → 3.x **with the reason it cannot be one jump** - Boot 2.x is `javax`, Boot 3.x is `jakarta`, and no release supports both
- [ ] Nothing modified. It should stop and ask before editing

`java check.java` on the untouched project prints:

```
  Modernization report   spring-petclinic-rest

  [FAIL]  Build       maven-surefire-plugin:2.22.2:test ... There are test failures
  [ -- ]  Tests       tests never ran
  [ -- ]  Startup     no jar was built

  What changed
     Java bytecode             8   (goal 21)
     Spring Boot               2.1.5.RELEASE
     Libraries                 0 of 87 at a different version

  Finished?   0 / 3
```

<!-- Bob-verify: this checklist comes from a real Bob run of the Korean prompt in _labs_ko, checked line by line against the source - the four file:line citations and every library version were correct. The English prompt above has not been run yet; run it and confirm it reaches the same answer. -->

## Tips

- Zero of three is the expected starting score. It means the project has not moved to Java 21 yet, not that the application is broken - the API console in Step 3 is the proof of that, which is why that step comes first.
- Step 4 and Step 5 are the same story. `The forked VM terminated without properly saying goodbye` in `build.log` is not a message anyone reasons their way out of, and Bob names jacoco `0.8.2` as the cause without being asked. If it does not, paste the line in and ask. (The exit code after it differs between JVMs - ignore the number.)
- If Bob answers in generalities - "update your dependencies, migrate to Jakarta EE" - it has not read the code. Ask for the versions in `pom.xml` and the files they land in, and it will go and look.
- Watch for whether `javax.sql.DataSource` comes up. "Rename every javax" and "rename every javax except this one" are the two answers this lab exists to tell apart.
- Ask why on the ordering. An upgrade plan you cannot defend to your own team is not a plan, and this is a question Bob answers well.
- Keep the answer. The next lab runs the upgrade, and comparing what happened against this plan is the fastest way to see what the workflow did on its own.

## Variations

1. **Your own repository**: the same prompt works unchanged on any Java project you have. Point Bob at it and ask for the same three things - what it is on, what breaks, what order.
2. **Only the security question**: `Which of this project's dependencies have known vulnerabilities? Give me the package, the pinned version, and the version that fixes it.` Useful on its own when the upgrade is not approved yet but somebody upstairs is asking about exposure.
3. **Estimate before commitment**: `Based on this assessment, what would you not attempt automatically, and what would you want a person to review?` A scope answer is easier to take into a planning meeting than a confidence answer.
