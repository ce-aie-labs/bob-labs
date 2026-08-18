---
title: Run the Java Upgrade Workflow End to End
lang: en
category: Migration
difficulty: Guided
duration: 60 min
stack: Java, Spring Boot
work_replaced: Manual Java and Spring Boot version upgrade
expected_saving: 1 week → 1 hour
---

## Problem

Moving a service from Java 8 to Java 21 is not one task. It is a dependency audit, a package rename across every file, a set of build plugins that have to be replaced before anything compiles, a round of vulnerable libraries to lift, and then however many days of fixing whatever broke. The steps are known. Doing them in order, for a week, is the part nobody volunteers for.

Bob's Java Modernization package runs those steps as a fixed workflow: it works out the dependencies itself, applies OpenRewrite recipes for the mechanical rewrites, looks up known vulnerabilities and repairs them as one of its own stages, and keeps building and fixing until it stops failing. This lab runs that workflow on a real application and then checks the result against the running app.

The workflow comes with Bob's Java Modernization premium package. Without it, do [Size Up a Legacy Java Application](../petclinic-assess/) instead - same application, twenty minutes, no package needed.

## Prompt

### Step 1 - Start from the assessed project

You need the unzipped `bob-lab-petclinic` folder from [Size Up a Legacy Java Application](../petclinic-assess/), with Java 21 installed and `java check.java` reporting `0 / 3`. That lab takes twenty minutes and its plan is what you will compare this run against.

Open the `app` folder in Bob.

### Step 2 - Start the workflow

Use the **Start Workflow** button, or the `/start-java-mod` skill, or ask for it in Agent mode:

```
Run the Java Upgrade workflow on this project.

Target: Java 21, Spring Boot 3.x
Build tool: Maven
Distribution: Semeru

Do not weaken the tests. Changing an import or an API call so it compiles is
fine. Deleting a test, disabling it, or dropping an assertion is not - if a
test fails, fix the code.

Do not change the REST contract: paths, field names and formats, HTTP status
codes, error message strings.
```

### Step 3 - Answer when it asks

The workflow stops and asks. This is the part people miss, so watch for it: it will report what it found and propose a target, and the proposal is not always the one you want. In recorded runs it offered to stop at Spring Boot 2.7 because that is the last release on the `javax` namespace - a defensible choice, and the wrong one if you asked for Boot 3.

When that happens, say where you actually want to land:

```
Go to Spring Boot 3.x, not 2.7. Use 2.7 as an intermediate step if you need one.
```

Answer its questions and let it continue. Expect four or five interruptions across the run and around forty minutes of wall time. You do not have to sit and watch, but do not walk away for an hour either.

### Step 4 - Watch what it does on its own

While it runs, look at the stage names in the task list rather than the diff. Recorded runs move through stages like `Fix errors preventing project build`, then **`Fix Vulnerabilities`**, then `Validate Fixes`. Nobody asked for the vulnerability stage - it is part of the workflow.

Three things are worth noticing as they happen:

- it reads the dependency tree and the Java environment itself, instead of asking you what the project uses
- it applies **OpenRewrite recipes** for the mechanical rewrites - the `javax` → `jakarta` rename and the deprecated-API replacements are a recipe run, not a model guessing file by file
- it **looks up known vulnerabilities** for the versions it lands on and lifts them, as its own stage rather than as a favour

### Step 5 - Check the result

When it finishes, back in the unzipped folder:

```
java check.java
```

This does not read Bob's summary. It builds the project, reads the class files, runs the tests, and starts the packaged application to ask it for a real response.

### Step 6 - Open the newly built application at the same address

**Stop the old application first** - Ctrl-C in the terminal from Step 3 of the first lab. It holds the same port, so the new one cannot start while it is up.

This time you start the jar Bob just built. `check.java` printed its name at the end of Step 5.

```
java -jar app/target/<the-name-it-printed>.jar
```

Then open the **same address** as in the first lab:

```
http://localhost:9966/petclinic/swagger-ui.html
```

That address has to still be there. Step 2 told the workflow not to change the REST contract, and an address that moved means every other system calling this API is broken.

**The page will look different.** The library that draws this console (springfox) cannot run on Spring Boot 3, so the workflow swapped it for a current one (springdoc) - a replacement nobody asked for. The skin is not the point. The endpoint list and the data behind it are: run `GET /vets` again and the same six vets come back.

### Step 7 - Hand back whatever is left

If any of the four lines is not green, give the workflow the file that explains it and let it keep going. That fix-build-fix loop is what the package is for, and stopping short of it is the most common way to waste the run.

```
The build is not finished yet. Here is the output - find the cause and fix it,
then build again and repeat until it passes. Do not weaken the tests.
```

Attach `build.log` for a build failure, `boot.log` if it builds but will not start.

## Expected Output

- [ ] A workflow with **named stages**, not one long edit - a task list that moves through building, fixing, vulnerabilities and validation, with `Fix Vulnerabilities` appearing without you asking for it
- [ ] **OpenRewrite recipes actually applied** - Bob names the recipes it ran, and the `javax` → `jakarta` rewrite lands across the codebase in one pass instead of file by file
- [ ] A **vulnerability lookup with results** - the packages it found, the versions it moved them to, and what it could not lift
- [ ] `java check.java` reporting the move as fact, not as a claim (the versions and counts depend on where the workflow landed):

```
  Modernization report   spring-petclinic-rest

  [ OK ]  Build         BUILD SUCCESS
  [ OK ]  Java version  87 classes at Java 21
  [ OK ]  Tests         169 passed · 0 disabled · 225 assertions
  [ OK ]  Startup       HTTP 200  /petclinic/api/vets

  What changed
     Spring Boot               2.1.5.RELEASE  →  3.5.3
     Libraries                 52 of 87 at a different version

  Finished?   4 / 4
```

- [ ] All **169 tests still running and passing**, with nothing disabled and no assertions dropped - `check.java` fails the Tests line if the count moved, so a green line here means the bar did not move
- [ ] The API console still opens at the same address and `GET /vets` returns the same six vets as before - even though the library rendering that console was replaced

<!-- Bob-verify: the workflow behaviour above - the stage names, the Boot 2.7 proposal in Step 3, the recipe and vulnerability stages - comes from 18 recorded runs of this premium workflow against this exact fixture, but those runs were driven with Korean prompts, so this English wording has not been run. The check.java output is real, captured by running the tool against a recorded successful result. Confirm the English prompt reaches the same workflow before using this with participants. -->

## Tips

- Step 3 is the lab. The workflow does the work, but where it lands is your call, and a target you did not choose is the easiest way to end up somewhere reasonable and wrong.
- Recorded runs did not all land in the same place from the same starting point. Treat the first result as a draft to check rather than a delivery, which is what Step 5 is for.
- Green tests are not a finished upgrade. The tests run in the build; the application starting is a separate fact, and the two come apart more often than you would expect.
- Read the vulnerability output rather than the count. Which packages moved matters more to whoever reviews this than how many findings disappeared.
- Do not merge the branch as the last step of the lab. The workflow gets you a compiling, tested, running upgrade - it does not replace a person signing off on behaviour.

## Variations

1. **Liberty replatforming**: `Run the Liberty Replatforming workflow on this project` - move a WebSphere application to Open Liberty, generate and validate `server.xml`, and deploy with the `liberty-maven-plugin`.
2. **Unit test generation**: `Run the Unit Test Generation workflow` - a strategy pass first, then generate-run-fix loops that iterate to a JaCoCo coverage threshold. Worth running on a legacy service *before* upgrading it, when the tests you would rely on do not exist yet.
3. **Security first, upgrade second**: `Fix the known vulnerabilities in this project's dependencies without changing the Java or Spring Boot version.` Sometimes the exposure is the urgent part and the version bump is next quarter's work.
4. **Your own service**: run the same workflow against a repository you own. The prompt above is unchanged apart from the target version - keep the two rules about tests and the REST contract, because those are what make the result reviewable.
