# NoctisUI Documentation

This directory contains the official documentation for NoctisUI, built with [VitePress](https://vitepress.dev/).

## 📁 Structure

```
docs/
├── .vitepress/          # VitePress configuration
│   └── config.mts       # Site configuration
├── api/                 # API reference documentation
│   └── components.md    # Components API
├── examples/            # Code examples and tutorials
│   ├── basic-button.md
│   ├── container-layout.md
│   └── custom-screen.md
├── guides/              # How-to guides
│   └── styling.md
├── public/              # Static assets
├── getting-started.md   # Getting started guide
└── index.md            # Home page
```

## 🚀 Development

### Prerequisites

- Node.js 18+ 
- npm

### Local Development

1. Install dependencies:
```bash
npm install
```

2. Start the dev server:
```bash
npm run docs:dev
```

The documentation will be available at your local server (usually port 5173).

### Build

Build the static site:
```bash
npm run docs:build
```

The built site will be in `docs/.vitepress/dist/`

### Preview Build

Preview the production build:
```bash
npm run docs:preview
```

## ✏️ Contributing to Documentation

### Adding a New Page

1. Create a new `.md` file in the appropriate directory
2. Add frontmatter if needed
3. Write your content in Markdown
4. Update `docs/.vitepress/config.mts` to add the page to navigation
5. Test locally with `npm run docs:dev`
6. Build to verify: `npm run docs:build`

### Markdown Features

VitePress supports enhanced Markdown:

- **Code blocks** with syntax highlighting
- **Custom containers** (tip, warning, danger, etc.)
- **Tables**
- **Emoji** :tada:
- **Math** equations
- And more!

See [VitePress Markdown](https://vitepress.dev/guide/markdown) for details.

### Style Guide

- Use clear, concise language
- Include code examples for all features
- Add links to related documentation
- Use proper heading hierarchy (h1 → h2 → h3)
- Keep code blocks under 50 lines when possible
- Add comments to complex code examples

## 📝 Documentation Standards

### Code Examples

- All Java code examples should be syntactically correct
- Include necessary imports
- Use meaningful variable names
- Add comments for complex logic
- Show both basic and advanced usage

### Links

- Use relative links for internal pages: `/api/components`
- Check for dead links before committing
- Use descriptive link text

### Images

- Place images in `docs/public/`
- Use descriptive alt text
- Optimize images for web

## 🔍 Verification

Before submitting documentation changes:

1. ✅ Build succeeds: `npm run docs:build`
2. ✅ No dead links
3. ✅ Code examples are correct
4. ✅ Links work in preview
5. ✅ Spelling and grammar checked

## 📄 License

This documentation is part of the NoctisUI project and is licensed under GPL-3.0.
