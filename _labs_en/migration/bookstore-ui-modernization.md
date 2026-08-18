---
title: Modernize a JSF UI to React
lang: en
category: Migration
difficulty: Guided
duration: 60 min
stack: Java, JSF
work_replaced: Rewriting a JSF UI as a REST + React app by hand
expected_saving: 2 weeks → 1 hour
---

## Problem

A JSF front end - server-rendered `.xhtml`, managed beans, no API layer - is a dead end for a modern UI. Moving it to a REST backend and a React front end by hand means re-deriving every screen's behavior and hoping you preserved the business rules. That is weeks of careful, error-prone work.

Bob's premium UI Modernization workflow splits the app into a Spring Boot REST backend and a React + Carbon front end, screen by screen, and claims to preserve the business logic exactly. This lab runs it on a real JSF app and checks both halves build - and that the logic really did survive. Note: this is a **paid premium package** - it only runs on accounts that have it enabled.

## Prompt

### Step 1 - Install Java 21, Maven, and Node

Java 21 + Maven as in the other labs. For the generated front end you also need **Node 18+** (`node -v`) - <https://nodejs.org> or `brew install node`.

### Step 2 - Get the application

Download **[bob-lab-bookstore-ui.zip](https://github.com/ce-aie-labs/bob-labs/releases/download/lab-assets/bob-lab-bookstore-ui.zip)** and unzip it. Open a terminal in the folder holding `START-HERE.md`.

This is **Legacy Bookstore** with **4 JSF screens** (`index`, book list, book detail, order confirm). It is the annotated build: the JSF beans already carry `@ManagedBean` / `@SessionScoped`, and there are empty `app/back/` and `app/front/` folders for the output - both required, see Tips.

### Step 3 - Confirm the start state

```
mvn clean package
```

`BUILD SUCCESS` on the JSF app is your baseline. The UI right now is the four `.xhtml` screens under `app/src/main/webapp`.

### Step 4 - Run the UI Modernization workflow

Open the `app` folder in Bob. Start the workflow and choose **UI Modernization**:

```
Run the UI Modernization workflow on this project.

Backend: Spring Boot REST endpoints in back/
Frontend: React + TypeScript + Carbon Design System in front/
Preserve all business rules - do not change service or repository logic.
Migrate screen by screen.
```

### Step 5 - Check both halves

```
cd back  && mvn -q clean package     # Spring Boot REST backend builds
cd ../front && npm install && npm run build   # React + Vite frontend builds
```

Then confirm the business logic was preserved - diff the service/repository classes against the originals (see Expected Output).

## Expected Output

- [ ] The 4 JSF screens became a **React front end** (a recorded run produced **8 React screens** on Carbon Design System) with a **Spring Boot REST backend** - `back/` and `front/` both build (the recorded `front/` built in **931 ms** with Vite)
- [ ] **Business rules preserved, verifiably** - the product claim is "preserving all original business rules," and in the recorded run the **service and repository layers were byte-identical** to the originals. Diff them; the logic should not have moved, only the UI and the API layer around it
- [ ] A real split, not a reskin: `.xhtml` + managed beans replaced by REST endpoints + React components, screen by screen - about **7,090 lines across 67 files** in the recorded run
- [ ] Both builds green on their own toolchains (Maven for `back/`, Vite for `front/`)

Seeded from a real run of this premium workflow against this exact fixture (JSF 4 screens → React 8 screens, byte-identical service/repo, 7,090 lines / 67 files, front build 931 ms), recorded in the IBM client-engineering Bob-for-Java evaluation.

<!-- Bob-verify: not yet run through Bob in this repo, and not runnable here - UI Modernization is part of the paid Java Modernization premium package we don't have access to. Numbers seeded from a real recorded run (VALUE.md: JSF 4→React 8 screens on Carbon, back/+front/ both build, service/repo byte-identical, 7090 lines/67 files). Run the EN prompt on the real package before use with participants; confirm the @ManagedBean gating and that the business logic diff is clean. -->

## Tips

- This is a premium package - UI Modernization runs on accounts with Java Modernization enabled. Confirm access before demoing.
- **This fixture is the annotated build for a reason.** The workflow **locks** unless the JSF managed beans carry `@ManagedBean` - that is why `bob-lab-bookstore-ui.zip` ships with the annotations already added and the empty `back/` / `front/` folders present. On a plain JSF app you would add those first.
- **Do UI Modernization before Java Upgrade, not after.** The order matters: UI Modernization → Java Upgrade works, but Java Upgrade → UI Modernization is blocked, because Jakarta Faces 4.0 removes `@ManagedBean` - the very annotation the UI workflow needs.
- **Run standalone, then plan the upgrade.** On its own the workflow kept the backend on Java 8 and introduced an EOL Spring Boot 2.7.18 (which a scan flagged with dozens of vulnerabilities). Treat the REST split and the version upgrade as two steps - modernize the UI, then upgrade and scan.
- Verify the business logic, not just that it compiles. The whole promise is "same rules, new UI" - the diff of the service/repository layer is where you confirm it.

## Variations

1. **Pair it with the upgrade**: UI-modernize first, then run the Java Upgrade lab on the new backend, then scan - the order the traps above force.
2. **Carbon target**: the workflow targets React + Carbon; ask it to map each JSF screen to specific Carbon components and check the mapping.
3. **Your own JSF/Struts app**: run it on a legacy UI you own - annotate the managed beans first, and diff the business layer after to confirm nothing moved.
