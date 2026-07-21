# Dev container

Runs the site without installing Ruby. Two ways in:

- **In the browser** - on GitHub, `Code` → `Codespaces` → create. Nothing to install.
  Codespaces has to be enabled for the organisation first; it is off by default,
  so this cannot bill anyone by surprise.
- **Locally** - VS Code with Docker, then `Reopen in Container`.

Either way, once it is up:

```sh
script/preview
```

The site is at port 4000 under **`/bob-labs/`** - the bare root 404s, because
the site is built for a project path rather than a domain root.

If you would rather not use a container, `script/preview` works directly on
your machine too; it just needs Ruby 3+, and will tell you if you do not have it.
