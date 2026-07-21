---
layout: event
lang: en
permalink: /bobathon/
title: Bobathon
description: Bring the work you do over and over. Spend the session replacing it with Bob, then submit what you built so the next person can reuse it.

need_heading: What you need
need_body: |
  One thing: **a piece of work you do repeatedly.** A report you rebuild every week, a review you run on every pull request, a migration you have already done four times. It does not need to be interesting - repetitive beats impressive, because repetitive is what pays back.

  If nothing comes to mind, look at your last two weeks. The task you were mildly annoyed to do a second time is the right one.

process_heading: The process
steps:
  - title: Pick the work
    body: |
      Write it as one sentence: *"Every week I ..."*. If the sentence needs an "and also", that is two tasks - pick one.

      Then note roughly how long it takes you today. You need that number at the end, and guessing it afterwards is how Before/After becomes fiction.
  - title: Set Bob up on it
    body: |
      Point Bob at the real repository or files, not a toy copy. Run `/init` first so it knows the project's structure and conventions before you ask for anything.

      If a lab in the library covers something close, start from its prompt rather than a blank box.
  - title: Work the loop
    body: |
      Prompt, read what came back, correct it, run again. The first output is rarely the one you keep, and that is normal - the skill being practised is the correcting, not the opening prompt.

      Note where Bob went wrong and what fixed it. That becomes your Tips section.
  - title: Measure it
    body: |
      Time the Bob version honestly, including the prompting and the corrections.

      `30 min → 7 min` is a result. So is `30 min → 25 min`, and it is more useful to share than a number nobody believes.

rubric_heading: How it is judged
rubric:
  - criterion: Was it genuinely repetitive work
    points: 20
  - criterion: Was Bob used well
    points: 30
  - criterion: Was there real time saved
    points: 20
  - criterion: Can someone else reuse it
    points: 30
rubric_note: |
  Reusability and applicability carry 60 of the 100 points on purpose. What this event produces is not a ranking of people - it is **assets**. A modest saving someone else can pick up beats an impressive one that only works for you.

deliverables_heading: What to submit
deliverables:
  - title: The one-line Before/After
    body: "Usually 30 min to Bob 7 min. If you submit nothing else, submit this."
    required: true
  - title: Your Bob chat history
    body: "The prompts and the corrections, so someone can see how you got there and not just where you landed."
  - title: Repository or file link
    body: "Where the work happened, if it can be shared."
  - title: What went wrong and what fixed it
    body: "A sentence or two. This is the part that turns a submission into a lab someone else can run."
deliverables_note: |
  Submit even if it did not work. A prompt that failed, with the reason, saves the next person the same hour.
---
