---
name: preview
description: Run the Bob Labs site locally and check a change actually renders. Use when asked to run, start, serve, preview or screenshot the site, or to confirm a lab, layout or CSS change looks right before opening a PR.
---

# Preview the site

The site is Jekyll. Markdown files are not viewable in a browser, and opening a
built file from `_site/` fails too - everything is served under `/bob-labs/`, so
absolute asset paths resolve to the disk root over `file://` and 404. Use the
server.

## Start it

```sh
script/preview
```

The script checks the Ruby version, installs dependencies on first run, and
prints the URLs. Do not hand-roll `bundle exec jekyll serve`; the script exists
because Jekyll needs Ruby 3+ while macOS ships 2.6, and `bundle install`
otherwise dies on a gem called `ffi` without mentioning Ruby.

| URL | |
|---|---|
| `http://127.0.0.1:4000/bob-labs/` | Redirects to `/en/` |
| `http://127.0.0.1:4000/bob-labs/en/` | English home - the lab library |
| `http://127.0.0.1:4000/bob-labs/ko/` | Korean home |
| `http://127.0.0.1:4000/bob-labs/en/bobathon/` | Event page |
| `http://127.0.0.1:4000/bob-labs/en/labs/<stack>/<slug>/` | A lab |

Both languages sit behind a prefix; neither is the default. The bare root is a redirect stub to `/en/`, and the URL *below* `/bob-labs/` 404s because the site is built for a project path.

Edits to `_labs_en/`, `_layouts/` and `assets/` rebuild automatically. Changes to
`_config.yml` need a restart.

## What to check

Always both languages and both themes. Toggle theme with the header control, or
`document.documentElement.setAttribute("data-theme", "light")`.

Reading a computed style straight after setting `data-theme` in the same
synchronous block returns the *old* value - the style recalc has not flushed.
Read it in a separate call.

By what changed:

- **A lab** - the card in the grid (does the title wrap badly, where does the
  auto-truncated preview cut off, does a long metadata value break the badge
  row), then the detail page. On a Challenge lab, hints must start collapsed,
  and opening one must produce a copy button - that only happens with
  `<details markdown="1">`; without the attribute Kramdown emits no `<pre>`.
- **Homepage filters** - counts update against the other group's selection,
  options that would return nothing are disabled, reset clears all three.
- **Bobathon** - the canvas/surface-1 band alternation, the four step cards,
  and the blue CTA banner. The submit button is deliberately disabled while
  `bobathon.submit_url` is empty in `_config.yml`.

## Check it against DESIGN.md

`DESIGN.md` is the Carbon spec this site follows, and it is specific. The rules
most easily broken:

- 0px corners everywhere - no rounding on buttons, cards or inputs
- Sentence case for labels and eyebrows, never all-caps tracked
- IBM Blue is scarce: links, the primary CTA, CTA banners, focus rings. Several
  blue elements at once is drift
- Depth comes from 1px hairlines and surface change, never drop shadows
- Weight 300 on display sizes - do not bold a display headline
- `letter-spacing: 0.16px` on body sizes

Measure contrast rather than eyeballing it, and remember that `opacity` on text
multiplies down whatever the underlying pair measures.

## Finish

Stop the server when done:

```sh
pkill -f "jekyll serve"
```

Leave no `_site/`, `vendor/` or `.bundle/` in a commit - they are gitignored,
but check `git status` anyway.
