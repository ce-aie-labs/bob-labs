---
layout: event
lang: en
permalink: /bobathon/
title: Bobathon
description: Bring the work you do over and over. Spend the session replacing it with Bob, then submit what you built so the next person can reuse it.
---

## What you need

One thing: **a piece of work you do repeatedly.** A report you rebuild every week, a review you run on every PR, a migration you have done four times already. It does not need to be interesting - repetitive beats impressive, because repetitive is what pays back.

If you cannot think of one, look at your last two weeks. The task you were mildly annoyed to do twice is the right one.

## The process

### 1. Pick the work

Write it in one sentence: *"Every week I ..."*. If the sentence needs an "and also", it is two tasks - pick one.

Then write down roughly how long it takes you today. You will need that number at the end, and guessing it afterwards is how Before/After becomes fiction.

### 2. Set Bob up on it

Point Bob at the real repository or files - not a toy copy. Run `/init` first so Bob knows the project's structure and conventions before you ask it for anything.

If a lab in the library already covers something close, start from its prompt rather than a blank box. That is what the library is for.

### 3. Work the loop

Prompt, look at what came back, correct it, run again. The first output is rarely the one you keep, and that is normal - the skill being practised here is the correcting, not the first prompt.

Keep an eye on where Bob went wrong and what fixed it. That becomes your Tips section later.

### 4. Measure it

Time the Bob version honestly, including the prompting and the corrections. `30 min → 7 min` is a result. `30 min → 25 min` is also a result, and a more useful one to share than a number nobody believes.

## How it is judged

| Criterion | Points |
|---|---|
| Was it genuinely repetitive work | 20 |
| Was Bob used well | 30 |
| Was there real time saved | 20 |
| Can someone else reuse it | 30 |

Reusability and applicability carry 60 of the 100 points on purpose. What this event produces is not a ranking of people - it is **assets**. A modest saving that someone else can pick up beats an impressive one that only works for you.

## What to submit

- [ ] **The one-line Before/After** - `usually 30 min → Bob 7 min`. This is the only required field.
- [ ] **Your Bob chat history** - the prompts and corrections, so someone can see how you got there, not just where you landed
- [ ] **Repository or file link** - where the work happened, if it can be shared
- [ ] **What went wrong and what fixed it** - a sentence or two. This is the part that turns your submission into a lab someone else can run.

Submit even if it did not work. A prompt that failed, with the reason, saves the next person the same hour.
