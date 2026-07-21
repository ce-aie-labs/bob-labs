## Summary

<!-- What does this PR do, and why? -->

## Type of change

- [ ] New Lab/Recipe asset (English + Korean pair)
- [ ] Docs / process change
- [ ] Site or tooling change (Jekyll, GitHub Actions, CSS)
- [ ] Bug fix

## If this adds or edits a Lab/Recipe asset

- [ ] Both `_labs/<stack>/<slug>.md` and `_labs_ko/<stack>/<slug>.md` are present
- [ ] `title`, `lang`, and all five metadata fields are filled in both files
- [ ] Full content spec in both files, no section skipped (`Problem → Prompt → Expected Output → Tips → Variations` / `문제 → 프롬프트 → 기대 결과 → 팁 → 응용`)
- [ ] Each language's Prompt was actually run through Bob; Expected Output reflects what was observed in that language, not a translation
- [ ] One asset per PR

## Before submitting

- [ ] `git status` confirms `NOTE.md` and `docs/` are not staged
- [ ] `build` and `validate` CI checks pass

See [CONTRIBUTING.md](../CONTRIBUTING.md) for the full guide.
