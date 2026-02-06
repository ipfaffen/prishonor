# Prishonor - TypeScript Implementation

This is the TypeScript/HTML5 Canvas implementation of the Prishonor game, migrated from the original Java/JavaFX version.

## Phase 1: Infrastructure Setup ✅

The foundation has been established with:

- ✅ TypeScript 5.3+ project configuration
- ✅ Vite build system and dev server
- ✅ Canvas-based rendering engine
- ✅ Game loop with requestAnimationFrame
- ✅ Keyboard input manager (arrow keys, space, enter)
- ✅ Stage data loading (JSON scaffold)
- ✅ Player profile storage (localStorage scaffold)
- ✅ Stage navigation scaffold (Tab to cycle)
- ✅ Stage preview renderer (visual placeholder)
- ✅ Basic project structure
- ✅ CSS styling and responsive layout

This includes working TypeScript entry points and core infrastructure files such as
`src/main.ts`, `src/game/Game.ts`, `src/game/GameLoop.ts`, `src/game/RenderEngine.ts`,
and shared types in `src/types/GameTypes.ts`.

## Project Structure

```
prishonor/
├── src/
│   ├── game/
│   │   ├── Game.ts              # Main game controller
│   │   ├── GameLoop.ts          # Game loop implementation
│   │   └── RenderEngine.ts      # Canvas rendering engine
│   ├── types/
│   │   └── GameTypes.ts         # TypeScript type definitions
│   ├── main.ts                  # Application entry point
│   └── style.css                # Global styles
├── index.html                   # HTML entry point
├── package.json                 # Dependencies and scripts
├── tsconfig.json                # TypeScript configuration
├── vite.config.ts               # Vite configuration
└── TYPESCRIPT_MIGRATION_PLAN.md # Complete migration plan

```

## Getting Started

### Prerequisites

- Node.js 18+ and npm/pnpm

### Installation

```bash
# Install dependencies
npm install

# Sync original Java assets (sprites + stage data)
npm run sync:assets

# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

## Development

### Available Scripts

- `npm run dev` - Start Vite dev server on port 3000
- `npm run build` - Build production bundle
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint
- `npm run format` - Format code with Prettier
- `npm test` - Run unit tests (Vitest)
- `npm run test:ui` - Run tests with UI
- `npm run test:e2e` - Run end-to-end tests (Playwright)

### Phase 1 Demo

The current implementation displays a welcome screen with:
- Animated pulsing text
- Running game loop confirmation
- FPS counter
- Proper canvas rendering

## Next Steps - Phase 2

The next phase will implement:

1. **Core Framework Migration**
   - Input management (keyboard events)
   - Asset loading system
   - Sprite system foundation

2. **Domain Model**
   - Stage loading from JSON
   - Player data structures
   - Storage implementation (LocalStorage)

3. **Animation System**
   - Sprite animations
   - Action sequences
   - Frame-based animations

See [TYPESCRIPT_MIGRATION_PLAN.md](./TYPESCRIPT_MIGRATION_PLAN.md) for the complete roadmap.

## Technology Stack

- **Language**: TypeScript 5.3
- **Runtime**: Browser (HTML5)
- **Rendering**: HTML5 Canvas 2D
- **Build Tool**: Vite 5.0
- **Package Manager**: npm
- **Testing**: Vitest + Playwright
- **Linting**: ESLint + TypeScript ESLint
- **Formatting**: Prettier

## Original Java Version

The original Java/JavaFX implementation can be found in the `src/main/java` directory. This TypeScript version aims to maintain the same gameplay experience while leveraging modern web technologies.

## Contributing

This is a migration project following the comprehensive plan outlined in `TYPESCRIPT_MIGRATION_PLAN.md`. Contributions should align with the phased approach defined in that document.

## License

MIT License - See [LICENSE](./LICENSE) file for details.
