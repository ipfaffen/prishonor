# Prishonor - Java to TypeScript Migration Plan

## Executive Summary

This document outlines a complete implementation plan to migrate the Prishonor game from Java/JavaFX to TypeScript with HTML5 Canvas/WebGL. The game is a sprite-based "snake-like" game with 29 stages, skills system, animations, and player progress tracking.

## Project Overview

### Current Architecture (Java/JavaFX)
- **Language**: Java 1.7
- **UI Framework**: JavaFX
- **Dependencies**: Jackson (JSON processing), JavaFX runtime
- **Structure**: ~60 Java classes organized in packages
- **Assets**: PNG sprite sheets, stage images, JSON configuration
- **Storage**: Local file-based player progress

### Target Architecture (TypeScript/HTML5)
- **Language**: TypeScript 5.x
- **Runtime**: Browser (HTML5)
- **Rendering**: HTML5 Canvas 2D (primary) with optional WebGL acceleration
- **Build Tool**: Vite
- **Package Manager**: npm/pnpm
- **Bundler**: Vite + Rollup
- **Testing**: Vitest + Playwright

## Phase 1: Project Setup & Infrastructure

### 1.1 Initialize TypeScript Project

```bash
# Project structure
prishonor-ts/
├── src/
│   ├── main.ts
│   ├── game/
│   ├── animation/
│   ├── unit/
│   ├── skill/
│   ├── layout/
│   ├── model/
│   ├── storage/
│   ├── util/
│   ├── types/
│   └── assets/
├── public/
│   ├── images/
│   └── data/
├── tests/
├── package.json
├── tsconfig.json
├── vite.config.ts
└── README.md
```

### 1.2 Dependencies

**Core Dependencies:**
```json
{
  "dependencies": {
    "canvas": "^2.11.2",
    "typescript": "^5.3.0"
  },
  "devDependencies": {
    "vite": "^5.0.0",
    "@types/node": "^20.0.0",
    "vitest": "^1.0.0",
    "playwright": "^1.40.0",
    "eslint": "^8.55.0",
    "@typescript-eslint/parser": "^6.15.0",
    "@typescript-eslint/eslint-plugin": "^6.15.0",
    "prettier": "^3.1.0"
  }
}
```

### 1.3 Configuration Files

**tsconfig.json:**
```json
{
  "compilerOptions": {
    "target": "ES2022",
    "module": "ESNext",
    "lib": ["ES2022", "DOM", "DOM.Iterable"],
    "moduleResolution": "bundler",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true,
    "resolveJsonModule": true,
    "outDir": "./dist",
    "baseUrl": "./src",
    "paths": {
      "@/*": ["./*"]
    }
  },
  "include": ["src/**/*"],
  "exclude": ["node_modules", "dist"]
}
```

**vite.config.ts:**
```typescript
import { defineConfig } from 'vite';
import { resolve } from 'path';

export default defineConfig({
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  build: {
    target: 'es2022',
    outDir: 'dist',
    assetsDir: 'assets',
    sourcemap: true
  },
  server: {
    port: 3000
  }
});
```

## Phase 2: Core Framework Migration

### 2.1 Rendering Engine (JavaFX → Canvas)

**Key Conversions:**
- `javafx.scene.Group` → Canvas rendering context
- `javafx.scene.Scene` → HTML Canvas element
- `javafx.scene.Node` → Custom Renderable interface
- `javafx.scene.paint.Color` → CSS color strings / RGB objects

**Implementation:**

```typescript
// src/game/rendering/RenderContext.ts
interface Renderable {
  render(ctx: CanvasRenderingContext2D): void;
  update(deltaTime: number): void;
}

class RenderEngine {
  private canvas: HTMLCanvasElement;
  private ctx: CanvasRenderingContext2D;
  private renderables: Renderable[] = [];
  private lastTime: number = 0;

  constructor(canvasId: string, width: number, height: number) {
    this.canvas = document.getElementById(canvasId) as HTMLCanvasElement;
    this.canvas.width = width;
    this.canvas.height = height;
    this.ctx = this.canvas.getContext('2d')!;
  }

  add(renderable: Renderable): void {
    this.renderables.push(renderable);
  }

  remove(renderable: Renderable): void {
    const index = this.renderables.indexOf(renderable);
    if (index > -1) {
      this.renderables.splice(index, 1);
    }
  }

  start(): void {
    requestAnimationFrame((time) => this.gameLoop(time));
  }

  private gameLoop(currentTime: number): void {
    const deltaTime = (currentTime - this.lastTime) / 1000;
    this.lastTime = currentTime;

    // Clear canvas
    this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

    // Update and render all renderables
    this.renderables.forEach(renderable => {
      renderable.update(deltaTime);
      renderable.render(this.ctx);
    });

    requestAnimationFrame((time) => this.gameLoop(time));
  }
}
```

### 2.2 Game Loop Architecture

**Java Pattern (JavaFX AnimationTimer) → TypeScript RequestAnimationFrame:**

```typescript
// src/game/GameLoop.ts
class GameLoop {
  private isRunning: boolean = false;
  private isPaused: boolean = false;
  private lastFrameTime: number = 0;
  private fps: number = 60;
  private frameInterval: number = 1000 / this.fps;

  constructor(
    private updateCallback: (deltaTime: number) => void,
    private renderCallback: () => void
  ) {}

  start(): void {
    if (this.isRunning) return;
    this.isRunning = true;
    this.lastFrameTime = performance.now();
    this.loop(this.lastFrameTime);
  }

  pause(): void {
    this.isPaused = true;
  }

  resume(): void {
    this.isPaused = false;
  }

  stop(): void {
    this.isRunning = false;
  }

  private loop(currentTime: number): void {
    if (!this.isRunning) return;

    const elapsed = currentTime - this.lastFrameTime;

    if (elapsed >= this.frameInterval) {
      const deltaTime = elapsed / 1000;
      
      if (!this.isPaused) {
        this.updateCallback(deltaTime);
      }
      
      this.renderCallback();
      this.lastFrameTime = currentTime - (elapsed % this.frameInterval);
    }

    requestAnimationFrame((time) => this.loop(time));
  }
}
```

### 2.3 Input Management

**Java KeyEvent → TypeScript KeyboardEvent:**

```typescript
// src/game/input/KeyboardManager.ts
enum KeyCode {
  ARROW_UP = 'ArrowUp',
  ARROW_DOWN = 'ArrowDown',
  ARROW_LEFT = 'ArrowLeft',
  ARROW_RIGHT = 'ArrowRight',
  SPACE = 'Space',
  TAB = 'Tab',
  ENTER = 'Enter'
}

class KeyboardManager {
  private keys: Map<string, boolean> = new Map();
  private keyHandlers: Map<string, () => void> = new Map();

  constructor() {
    this.setupEventListeners();
  }

  private setupEventListeners(): void {
    window.addEventListener('keydown', (e) => this.onKeyDown(e));
    window.addEventListener('keyup', (e) => this.onKeyUp(e));
  }

  private onKeyDown(event: KeyboardEvent): void {
    event.preventDefault();
    const key = event.code;
    
    if (!this.keys.get(key)) {
      this.keys.set(key, true);
      
      const handler = this.keyHandlers.get(key);
      if (handler) {
        handler();
      }
    }
  }

  private onKeyUp(event: KeyboardEvent): void {
    this.keys.set(event.code, false);
  }

  isKeyPressed(key: string): boolean {
    return this.keys.get(key) || false;
  }

  registerKeyHandler(key: string, handler: () => void): void {
    this.keyHandlers.set(key, handler);
  }

  unregisterKeyHandler(key: string): void {
    this.keyHandlers.delete(key);
  }

  dispose(): void {
    window.removeEventListener('keydown', (e) => this.onKeyDown(e));
    window.removeEventListener('keyup', (e) => this.onKeyUp(e));
    this.keys.clear();
    this.keyHandlers.clear();
  }
}
```

## Phase 3: Domain Model Migration

### 3.1 Type Definitions

```typescript
// src/types/GameTypes.ts
export interface Position {
  x: number;
  y: number;
}

export interface Size {
  width: number;
  height: number;
}

export interface Bounds extends Position, Size {}

export interface Color {
  r: number;
  g: number;
  b: number;
  a?: number;
}

export enum Direction {
  UP = 'UP',
  DOWN = 'DOWN',
  LEFT = 'LEFT',
  RIGHT = 'RIGHT'
}

export enum SkillType {
  MAGIC_PROJECTILE = 'MAGIC_PROJECTILE',
  PORTAL = 'PORTAL'
}

export enum ModeEnum {
  STAGE = 'STAGE',
  MENU = 'MENU'
}

// src/types/StageTypes.ts
export interface StageObject {
  x: number;
  y: number;
  imageName: string;
}

export interface Stage {
  id: number;
  backgroundImageName: string;
  stageObjects: StageObject[];
  bronzeMedalPoints: number;
  silverMedalPoints: number;
  goldMedalPoints: number;
}

// src/types/PlayerTypes.ts
export interface PlayerStageScore {
  stageId: number;
  score: number;
}

export interface Player {
  lastPlayedStageId: number;
  stageScores: Map<number, number>;
}
```

### 3.2 Entity Classes

```typescript
// src/model/entity/Stage.ts
export class StageEntity {
  constructor(
    public id: number,
    public backgroundImageName: string,
    public stageObjects: StageObject[],
    public bronzeMedalPoints: number,
    public silverMedalPoints: number,
    public goldMedalPoints: number
  ) {}

  static fromJSON(data: any): StageEntity {
    return new StageEntity(
      data.id,
      data.backgroundImageName,
      data.stageObjects,
      data.bronzeMedalPoints,
      data.silverMedalPoints,
      data.goldMedalPoints
    );
  }
}

// src/model/entity/Background.ts
export class Background implements Renderable {
  private image: HTMLImageElement;
  private loaded: boolean = false;

  constructor(
    private imagePath: string,
    private position: Position,
    private size: Size
  ) {
    this.loadImage();
  }

  private loadImage(): void {
    this.image = new Image();
    this.image.onload = () => {
      this.loaded = true;
    };
    this.image.src = this.imagePath;
  }

  render(ctx: CanvasRenderingContext2D): void {
    if (!this.loaded) return;
    
    ctx.drawImage(
      this.image,
      this.position.x,
      this.position.y,
      this.size.width,
      this.size.height
    );
  }

  update(deltaTime: number): void {
    // Background typically doesn't need updates
  }
}
```

### 3.3 Player & Storage

```typescript
// src/storage/Storage.ts
export class Storage {
  private static readonly PLAYER_KEY = 'prishonor_player';

  static savePlayer(player: Player): void {
    const data = {
      lastPlayedStageId: player.lastPlayedStageId,
      stageScores: Array.from(player.stageScores.entries())
    };
    localStorage.setItem(this.PLAYER_KEY, JSON.stringify(data));
  }

  static loadPlayer(): Player {
    const data = localStorage.getItem(this.PLAYER_KEY);
    
    if (!data) {
      return this.createDefaultPlayer();
    }

    try {
      const parsed = JSON.parse(data);
      return {
        lastPlayedStageId: parsed.lastPlayedStageId,
        stageScores: new Map(parsed.stageScores)
      };
    } catch (error) {
      console.error('Error loading player data:', error);
      return this.createDefaultPlayer();
    }
  }

  private static createDefaultPlayer(): Player {
    return {
      lastPlayedStageId: 1,
      stageScores: new Map()
    };
  }
}
```

## Phase 4: Animation System

### 4.1 Sprite System

```typescript
// src/animation/SpriteImage.ts
export interface SpriteFrame {
  x: number;
  y: number;
  width: number;
  height: number;
}

export class SpriteImage {
  private image: HTMLImageElement;
  private loaded: boolean = false;

  constructor(
    private imagePath: string,
    private frames: SpriteFrame[]
  ) {
    this.loadImage();
  }

  private loadImage(): void {
    this.image = new Image();
    this.image.onload = () => {
      this.loaded = true;
    };
    this.image.src = this.imagePath;
  }

  isLoaded(): boolean {
    return this.loaded;
  }

  getImage(): HTMLImageElement {
    return this.image;
  }

  getFrame(index: number): SpriteFrame | undefined {
    return this.frames[index];
  }

  getFrameCount(): number {
    return this.frames.length;
  }
}

// src/animation/SpriteAnimation.ts
export class SpriteAnimation {
  private currentFrame: number = 0;
  private elapsedTime: number = 0;
  private isPlaying: boolean = false;
  private loop: boolean = true;

  constructor(
    private spriteImage: SpriteImage,
    private frameSequence: number[],
    private frameDuration: number // in seconds
  ) {}

  play(): void {
    this.isPlaying = true;
  }

  pause(): void {
    this.isPlaying = false;
  }

  stop(): void {
    this.isPlaying = false;
    this.currentFrame = 0;
    this.elapsedTime = 0;
  }

  setLoop(loop: boolean): void {
    this.loop = loop;
  }

  update(deltaTime: number): void {
    if (!this.isPlaying) return;

    this.elapsedTime += deltaTime;

    if (this.elapsedTime >= this.frameDuration) {
      this.elapsedTime = 0;
      this.currentFrame++;

      if (this.currentFrame >= this.frameSequence.length) {
        if (this.loop) {
          this.currentFrame = 0;
        } else {
          this.currentFrame = this.frameSequence.length - 1;
          this.isPlaying = false;
        }
      }
    }
  }

  getCurrentFrameIndex(): number {
    return this.frameSequence[this.currentFrame];
  }

  isAnimating(): boolean {
    return this.isPlaying;
  }
}

// src/layout/SpriteElement.ts
export abstract class SpriteElement implements Renderable {
  protected position: Position = { x: 0, y: 0 };
  protected animation: SpriteAnimation;
  protected visible: boolean = true;

  constructor(
    protected spriteImage: SpriteImage,
    protected size: Size
  ) {}

  setPosition(x: number, y: number): void {
    this.position.x = x;
    this.position.y = y;
  }

  getPosition(): Position {
    return { ...this.position };
  }

  setVisible(visible: boolean): void {
    this.visible = visible;
  }

  render(ctx: CanvasRenderingContext2D): void {
    if (!this.visible || !this.spriteImage.isLoaded()) return;

    const frameIndex = this.animation.getCurrentFrameIndex();
    const frame = this.spriteImage.getFrame(frameIndex);

    if (!frame) return;

    const image = this.spriteImage.getImage();
    ctx.drawImage(
      image,
      frame.x, frame.y, frame.width, frame.height,
      this.position.x, this.position.y, this.size.width, this.size.height
    );
  }

  update(deltaTime: number): void {
    if (this.animation) {
      this.animation.update(deltaTime);
    }
  }
}
```

### 4.2 Action System

```typescript
// src/animation/ActionAnimation.ts
export interface Action {
  execute(deltaTime: number): boolean; // returns true when complete
  reset(): void;
}

export class MoveAction implements Action {
  private elapsed: number = 0;
  private startPos: Position;
  private currentPos: Position;

  constructor(
    private target: { position: Position },
    private endPos: Position,
    private duration: number
  ) {
    this.startPos = { ...target.position };
    this.currentPos = { ...target.position };
  }

  execute(deltaTime: number): boolean {
    this.elapsed += deltaTime;
    const progress = Math.min(this.elapsed / this.duration, 1);

    this.currentPos.x = this.startPos.x + (this.endPos.x - this.startPos.x) * progress;
    this.currentPos.y = this.startPos.y + (this.endPos.y - this.startPos.y) * progress;

    this.target.position.x = this.currentPos.x;
    this.target.position.y = this.currentPos.y;

    return progress >= 1;
  }

  reset(): void {
    this.elapsed = 0;
    this.currentPos = { ...this.startPos };
  }
}

export class ActionSequence {
  private currentActionIndex: number = 0;
  private isPlaying: boolean = false;
  private onComplete?: () => void;

  constructor(private actions: Action[]) {}

  play(onComplete?: () => void): void {
    this.isPlaying = true;
    this.currentActionIndex = 0;
    this.onComplete = onComplete;
  }

  update(deltaTime: number): void {
    if (!this.isPlaying || this.currentActionIndex >= this.actions.length) {
      return;
    }

    const currentAction = this.actions[this.currentActionIndex];
    const isComplete = currentAction.execute(deltaTime);

    if (isComplete) {
      this.currentActionIndex++;

      if (this.currentActionIndex >= this.actions.length) {
        this.isPlaying = false;
        if (this.onComplete) {
          this.onComplete();
        }
      }
    }
  }

  stop(): void {
    this.isPlaying = false;
    this.currentActionIndex = 0;
    this.actions.forEach(action => action.reset());
  }
}
```

## Phase 5: Game Logic Migration

### 5.1 Unit System

```typescript
// src/unit/Unit.ts
export abstract class Unit extends SpriteElement {
  protected health: number = 100;
  protected speed: number = 1;
  protected direction: Direction = Direction.RIGHT;

  abstract move(direction: Direction): void;
  abstract attack(): void;

  getHealth(): number {
    return this.health;
  }

  setHealth(health: number): void {
    this.health = Math.max(0, health);
  }

  isAlive(): boolean {
    return this.health > 0;
  }
}

// src/unit/UnitHero.ts
export class UnitHero extends Unit {
  private moveQueue: Direction[] = [];
  private isMoving: boolean = false;
  private currentSkill?: Skill;

  constructor(spriteImage: SpriteImage, size: Size) {
    super(spriteImage, size);
  }

  move(direction: Direction): void {
    if (this.isMoving) {
      this.moveQueue.push(direction);
      return;
    }

    this.performMove(direction);
  }

  private performMove(direction: Direction): void {
    this.isMoving = true;
    this.direction = direction;

    const targetPos = this.calculateTargetPosition(direction);
    const moveAction = new MoveAction(this, targetPos, 0.2);

    // Execute move with callback
    // Implementation depends on your action system
  }

  private calculateTargetPosition(direction: Direction): Position {
    const tileSize = 40; // Example tile size
    const pos = this.getPosition();

    switch (direction) {
      case Direction.UP:
        return { x: pos.x, y: pos.y - tileSize };
      case Direction.DOWN:
        return { x: pos.x, y: pos.y + tileSize };
      case Direction.LEFT:
        return { x: pos.x - tileSize, y: pos.y };
      case Direction.RIGHT:
        return { x: pos.x + tileSize, y: pos.y };
      default:
        return pos;
    }
  }

  attack(): void {
    if (this.currentSkill) {
      this.currentSkill.activate(this.position, this.direction);
    }
  }

  setSkill(skill: Skill): void {
    this.currentSkill = skill;
  }

  disable(): void {
    this.isMoving = false;
    this.moveQueue = [];
  }
}

// src/unit/UnitAssistant.ts
export class UnitAssistant extends Unit {
  private rescued: boolean = false;

  constructor(spriteImage: SpriteImage, size: Size, position: Position) {
    super(spriteImage, size);
    this.setPosition(position.x, position.y);
  }

  move(direction: Direction): void {
    // Assistants typically follow the hero or remain stationary
  }

  attack(): void {
    // Assistants don't attack
  }

  rescue(): void {
    this.rescued = true;
    // Trigger rescue animation
  }

  isRescued(): boolean {
    return this.rescued;
  }
}
```

### 5.2 Skills System

```typescript
// src/skill/Skill.ts
export abstract class Skill {
  protected cooldown: number = 0;
  protected cooldownDuration: number;

  constructor(
    public type: SkillType,
    cooldownDuration: number
  ) {
    this.cooldownDuration = cooldownDuration;
  }

  abstract activate(position: Position, direction: Direction): void;

  update(deltaTime: number): void {
    if (this.cooldown > 0) {
      this.cooldown = Math.max(0, this.cooldown - deltaTime);
    }
  }

  canActivate(): boolean {
    return this.cooldown <= 0;
  }

  protected startCooldown(): void {
    this.cooldown = this.cooldownDuration;
  }
}

// src/skill/MagicProjectile.ts
export class MagicProjectileSkill extends Skill {
  constructor(private arena: GameArena) {
    super(SkillType.MAGIC_PROJECTILE, 2.0);
  }

  activate(position: Position, direction: Direction): void {
    if (!this.canActivate()) return;

    const projectile = new MagicProjectile(position, direction);
    this.arena.addProjectile(projectile);
    
    this.startCooldown();
  }
}

// src/element/ElementMagicProjectile.ts
export class MagicProjectile implements Renderable {
  private velocity: Position;
  private active: boolean = true;

  constructor(
    private position: Position,
    direction: Direction,
    private speed: number = 200
  ) {
    this.velocity = this.calculateVelocity(direction);
  }

  private calculateVelocity(direction: Direction): Position {
    switch (direction) {
      case Direction.UP:
        return { x: 0, y: -this.speed };
      case Direction.DOWN:
        return { x: 0, y: this.speed };
      case Direction.LEFT:
        return { x: -this.speed, y: 0 };
      case Direction.RIGHT:
        return { x: this.speed, y: 0 };
    }
  }

  update(deltaTime: number): void {
    this.position.x += this.velocity.x * deltaTime;
    this.position.y += this.velocity.y * deltaTime;

    // Check bounds and collisions
    // Deactivate if out of bounds
  }

  render(ctx: CanvasRenderingContext2D): void {
    if (!this.active) return;
    
    // Render projectile sprite
    ctx.fillStyle = '#00ff00';
    ctx.fillRect(this.position.x, this.position.y, 10, 10);
  }

  isActive(): boolean {
    return this.active;
  }

  deactivate(): void {
    this.active = false;
  }
}
```

### 5.3 Game Arena

```typescript
// src/game/GameArena.ts
export class GameArena implements Renderable {
  private background?: Background;
  private hero?: UnitHero;
  private assistants: UnitAssistant[] = [];
  private projectiles: MagicProjectile[] = [];
  private obstacles: StageObject[] = [];

  constructor(
    private size: Size,
    private stage: StageEntity
  ) {
    this.initializeArena();
  }

  private initializeArena(): void {
    // Load background
    this.background = new Background(
      `/images/stage/${this.stage.backgroundImageName}`,
      { x: 0, y: 0 },
      this.size
    );

    // Place obstacles from stage objects
    this.obstacles = this.stage.stageObjects.map(obj => ({
      ...obj,
      position: { x: obj.x, y: obj.y }
    }));
  }

  addHero(hero: UnitHero): void {
    this.hero = hero;
  }

  addAssistant(assistant: UnitAssistant): void {
    this.assistants.push(assistant);
  }

  addProjectile(projectile: MagicProjectile): void {
    this.projectiles.push(projectile);
  }

  checkCollision(position: Position): boolean {
    // Check collision with obstacles
    for (const obstacle of this.obstacles) {
      if (this.isColliding(position, obstacle.position)) {
        return true;
      }
    }
    return false;
  }

  private isColliding(pos1: Position, pos2: Position): boolean {
    const threshold = 40; // tile size
    return Math.abs(pos1.x - pos2.x) < threshold &&
           Math.abs(pos1.y - pos2.y) < threshold;
  }

  update(deltaTime: number): void {
    if (this.hero) {
      this.hero.update(deltaTime);
    }

    this.assistants.forEach(assistant => assistant.update(deltaTime));
    
    // Update and remove inactive projectiles
    this.projectiles = this.projectiles.filter(projectile => {
      projectile.update(deltaTime);
      return projectile.isActive();
    });

    // Check for hero-assistant collisions (rescue)
    this.checkRescueCollisions();
  }

  private checkRescueCollisions(): void {
    if (!this.hero) return;

    const heroPos = this.hero.getPosition();
    
    this.assistants.forEach(assistant => {
      if (!assistant.isRescued() && this.isColliding(heroPos, assistant.getPosition())) {
        assistant.rescue();
      }
    });
  }

  render(ctx: CanvasRenderingContext2D): void {
    if (this.background) {
      this.background.render(ctx);
    }

    this.obstacles.forEach(obstacle => {
      // Render obstacle (simplified)
      ctx.fillStyle = '#808080';
      ctx.fillRect(obstacle.x, obstacle.y, 40, 40);
    });

    this.assistants.forEach(assistant => assistant.render(ctx));
    this.projectiles.forEach(projectile => projectile.render(ctx));

    if (this.hero) {
      this.hero.render(ctx);
    }
  }
}
```

### 5.4 Game Main Class

```typescript
// src/game/Game.ts
export class Game {
  private static instance: Game;
  
  private renderEngine: RenderEngine;
  private gameLoop: GameLoop;
  private keyboardManager: KeyboardManager;
  
  private player: Player;
  private stages: Map<number, StageEntity>;
  private currentStage?: StageEntity;
  
  private arena?: GameArena;
  private hero?: UnitHero;
  private skills: Map<SkillType, Skill>;
  
  private properties: GameProperties;
  private chronometer: Chronometer;
  private isPlaying: boolean = false;

  private constructor() {
    this.renderEngine = new RenderEngine('gameCanvas', 600, 430);
    this.gameLoop = new GameLoop(
      (dt) => this.update(dt),
      () => this.render()
    );
    this.keyboardManager = new KeyboardManager();
    this.skills = new Map();
    this.properties = new GameProperties();
    this.chronometer = new Chronometer();
  }

  static getInstance(): Game {
    if (!Game.instance) {
      Game.instance = new Game();
    }
    return Game.instance;
  }

  async initialize(): Promise<void> {
    // Load player data
    this.player = Storage.loadPlayer();

    // Load stages from JSON
    this.stages = await this.loadStages();

    // Prepare first/last played stage
    await this.prepareStage(this.player.lastPlayedStageId);

    // Setup keyboard controls
    this.setupKeyboardControls();

    // Start game loop
    this.gameLoop.start();
  }

  private async loadStages(): Promise<Map<number, StageEntity>> {
    const response = await fetch('/data/stages.json');
    const stagesData = await response.json();
    
    const stagesMap = new Map<number, StageEntity>();
    stagesData.forEach((stageData: any) => {
      const stage = StageEntity.fromJSON(stageData);
      stagesMap.set(stage.id, stage);
    });
    
    return stagesMap;
  }

  private async prepareStage(stageId: number): Promise<void> {
    this.currentStage = this.stages.get(stageId);
    if (!this.currentStage) {
      throw new Error(`Stage ${stageId} not found`);
    }

    // Create arena
    this.arena = new GameArena({ width: 600, height: 350 }, this.currentStage);

    // Create hero
    const heroSprite = await this.loadHeroSprite();
    this.hero = new UnitHero(heroSprite, { width: 40, height: 40 });
    this.hero.setPosition(100, 100);
    this.arena.addHero(this.hero);

    // Setup skills
    this.setupSkills();

    // Reset properties
    this.properties.reset();
    this.chronometer.reset();
  }

  private setupSkills(): void {
    if (!this.arena) return;

    this.skills.set(
      SkillType.MAGIC_PROJECTILE,
      new MagicProjectileSkill(this.arena)
    );
    // Add more skills as needed
  }

  private setupKeyboardControls(): void {
    this.keyboardManager.registerKeyHandler(KeyCode.ARROW_UP, () => {
      if (this.hero && !this.isPlaying) {
        this.startPlaying();
      }
      this.hero?.move(Direction.UP);
    });

    this.keyboardManager.registerKeyHandler(KeyCode.ARROW_DOWN, () => {
      if (this.hero && !this.isPlaying) {
        this.startPlaying();
      }
      this.hero?.move(Direction.DOWN);
    });

    this.keyboardManager.registerKeyHandler(KeyCode.ARROW_LEFT, () => {
      if (this.hero && !this.isPlaying) {
        this.startPlaying();
      }
      this.hero?.move(Direction.LEFT);
    });

    this.keyboardManager.registerKeyHandler(KeyCode.ARROW_RIGHT, () => {
      if (this.hero && !this.isPlaying) {
        this.startPlaying();
      }
      this.hero?.move(Direction.RIGHT);
    });

    this.keyboardManager.registerKeyHandler(KeyCode.SPACE, () => {
      this.hero?.attack();
    });

    this.keyboardManager.registerKeyHandler(KeyCode.ENTER, () => {
      this.restart();
    });
  }

  private startPlaying(): void {
    if (this.isPlaying) return;
    
    this.isPlaying = true;
    this.chronometer.start();
  }

  private update(deltaTime: number): void {
    if (!this.isPlaying) return;

    this.arena?.update(deltaTime);
    
    // Update skills
    this.skills.forEach(skill => skill.update(deltaTime));

    // Check win/lose conditions
    this.checkGameConditions();
  }

  private render(): void {
    this.arena?.render(this.renderEngine.getContext());
    // Render UI, toolbar, menu, etc.
  }

  private checkGameConditions(): void {
    if (!this.hero?.isAlive()) {
      this.gameOver();
    }

    // Check if all assistants rescued
    // etc.
  }

  private gameOver(): void {
    this.isPlaying = false;
    this.chronometer.stop();
    
    // Save score
    const score = this.properties.getPoints();
    this.player.stageScores.set(this.currentStage!.id, score);
    Storage.savePlayer(this.player);

    // Show game over screen
  }

  restart(): void {
    this.prepareStage(this.currentStage!.id);
  }

  nextStage(): void {
    const nextId = this.currentStage!.id + 1;
    if (this.stages.has(nextId)) {
      this.prepareStage(nextId);
    }
  }

  prevStage(): void {
    const prevId = this.currentStage!.id - 1;
    if (this.stages.has(prevId)) {
      this.prepareStage(prevId);
    }
  }
}
```

### 5.5 Main Entry Point

```typescript
// src/main.ts
import { Game } from './game/Game';
import './style.css';

async function main() {
  try {
    // Create HTML structure
    createGameUI();

    // Initialize and start game
    const game = Game.getInstance();
    await game.initialize();
  } catch (error) {
    console.error('Failed to start game:', error);
    showError('Failed to load game. Please refresh the page.');
  }
}

function createGameUI(): void {
  const app = document.getElementById('app')!;
  
  app.innerHTML = `
    <div class="game-container">
      <div class="game-header">
        <h1>Prishonor</h1>
      </div>
      <canvas id="gameCanvas"></canvas>
      <div class="game-footer">
        <div class="controls">
          <p>Arrow Keys: Move | Space: Use Skill | Tab: Change Skill | Enter: Restart</p>
        </div>
      </div>
    </div>
  `;
}

function showError(message: string): void {
  const app = document.getElementById('app')!;
  app.innerHTML = `
    <div class="error-container">
      <h2>Error</h2>
      <p>${message}</p>
    </div>
  `;
}

// Start the game
main();
```

## Phase 6: Utilities & Helpers

### 6.1 Utility Classes

```typescript
// src/util/Chronometer.ts
export class Chronometer {
  private startTime: number = 0;
  private elapsedTime: number = 0;
  private isRunning: boolean = false;

  start(): void {
    this.startTime = Date.now();
    this.isRunning = true;
  }

  stop(): void {
    if (this.isRunning) {
      this.elapsedTime += Date.now() - this.startTime;
      this.isRunning = false;
    }
  }

  reset(): void {
    this.startTime = 0;
    this.elapsedTime = 0;
    this.isRunning = false;
  }

  getElapsed(): number {
    if (this.isRunning) {
      return this.elapsedTime + (Date.now() - this.startTime);
    }
    return this.elapsedTime;
  }

  getElapsedSeconds(): number {
    return Math.floor(this.getElapsed() / 1000);
  }
}

// src/util/Position.ts
export class PositionUtil {
  static distance(pos1: Position, pos2: Position): number {
    const dx = pos2.x - pos1.x;
    const dy = pos2.y - pos1.y;
    return Math.sqrt(dx * dx + dy * dy);
  }

  static direction(from: Position, to: Position): Position {
    const dx = to.x - from.x;
    const dy = to.y - from.y;
    const length = Math.sqrt(dx * dx + dy * dy);
    
    if (length === 0) return { x: 0, y: 0 };
    
    return {
      x: dx / length,
      y: dy / length
    };
  }

  static lerp(start: Position, end: Position, t: number): Position {
    return {
      x: start.x + (end.x - start.x) * t,
      y: start.y + (end.y - start.y) * t
    };
  }
}

// src/util/DataList.ts
export class DataList<T> {
  private items: T[] = [];

  add(item: T): void {
    this.items.push(item);
  }

  remove(item: T): boolean {
    const index = this.items.indexOf(item);
    if (index > -1) {
      this.items.splice(index, 1);
      return true;
    }
    return false;
  }

  get(index: number): T | undefined {
    return this.items[index];
  }

  size(): number {
    return this.items.length;
  }

  clear(): void {
    this.items = [];
  }

  forEach(callback: (item: T, index: number) => void): void {
    this.items.forEach(callback);
  }

  filter(predicate: (item: T) => boolean): T[] {
    return this.items.filter(predicate);
  }

  toArray(): T[] {
    return [...this.items];
  }
}
```

## Phase 7: Asset Management

### 7.1 Asset Loader

```typescript
// src/assets/AssetLoader.ts
export class AssetLoader {
  private static imageCache: Map<string, HTMLImageElement> = new Map();
  private static audioCache: Map<string, HTMLAudioElement> = new Map();

  static async loadImage(path: string): Promise<HTMLImageElement> {
    if (this.imageCache.has(path)) {
      return this.imageCache.get(path)!;
    }

    return new Promise((resolve, reject) => {
      const img = new Image();
      img.onload = () => {
        this.imageCache.set(path, img);
        resolve(img);
      };
      img.onerror = () => {
        reject(new Error(`Failed to load image: ${path}`));
      };
      img.src = path;
    });
  }

  static async loadImages(paths: string[]): Promise<HTMLImageElement[]> {
    return Promise.all(paths.map(path => this.loadImage(path)));
  }

  static async loadJSON<T>(path: string): Promise<T> {
    const response = await fetch(path);
    if (!response.ok) {
      throw new Error(`Failed to load JSON: ${path}`);
    }
    return response.json();
  }

  static clearCache(): void {
    this.imageCache.clear();
    this.audioCache.clear();
  }
}
```

## Phase 8: UI Components

### 8.1 HTML Structure

```html
<!-- public/index.html -->
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Prishonor</title>
</head>
<body>
  <div id="app"></div>
  <script type="module" src="/src/main.ts"></script>
</body>
</html>
```

### 8.2 CSS Styling

```css
/* src/style.css */
:root {
  --bg-color: #1a1a1a;
  --text-color: #b9b9b9;
  --accent-color: #4a9eff;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  background-color: var(--bg-color);
  color: var(--text-color);
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.game-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.game-header h1 {
  font-size: 2.5rem;
  color: var(--accent-color);
}

#gameCanvas {
  border: 2px solid var(--accent-color);
  background-color: #000;
}

.game-footer {
  text-align: center;
}

.controls {
  background-color: rgba(255, 255, 255, 0.1);
  padding: 10px 20px;
  border-radius: 8px;
}

.error-container {
  text-align: center;
  padding: 40px;
}

.error-container h2 {
  color: #ff4444;
  margin-bottom: 20px;
}
```

## Phase 9: Testing Strategy

### 9.1 Unit Tests

```typescript
// tests/unit/Position.test.ts
import { describe, it, expect } from 'vitest';
import { PositionUtil } from '@/util/Position';

describe('PositionUtil', () => {
  it('should calculate distance correctly', () => {
    const pos1 = { x: 0, y: 0 };
    const pos2 = { x: 3, y: 4 };
    expect(PositionUtil.distance(pos1, pos2)).toBe(5);
  });

  it('should interpolate positions', () => {
    const start = { x: 0, y: 0 };
    const end = { x: 10, y: 10 };
    const mid = PositionUtil.lerp(start, end, 0.5);
    expect(mid).toEqual({ x: 5, y: 5 });
  });
});
```

### 9.2 Integration Tests

```typescript
// tests/integration/Game.test.ts
import { describe, it, expect, beforeEach } from 'vitest';
import { Game } from '@/game/Game';

describe('Game Integration', () => {
  let game: Game;

  beforeEach(async () => {
    game = Game.getInstance();
    await game.initialize();
  });

  it('should initialize with first stage', () => {
    expect(game.getCurrentStage()).toBeDefined();
    expect(game.getCurrentStage()?.id).toBe(1);
  });

  it('should advance to next stage', () => {
    game.nextStage();
    expect(game.getCurrentStage()?.id).toBe(2);
  });
});
```

## Phase 10: Build & Deployment

### 10.1 Build Configuration

```json
// package.json scripts
{
  "scripts": {
    "dev": "vite",
    "build": "tsc && vite build",
    "preview": "vite preview",
    "test": "vitest",
    "test:ui": "vitest --ui",
    "test:e2e": "playwright test",
    "lint": "eslint src --ext .ts,.tsx",
    "format": "prettier --write \"src/**/*.{ts,tsx,css}\""
  }
}
```

### 10.2 Deployment

**Static hosting options:**
- GitHub Pages
- Netlify
- Vercel
- Cloudflare Pages

## Migration Checklist

### Core Infrastructure
- [x] Project setup with TypeScript + Vite
- [x] Canvas rendering engine
- [x] Game loop with requestAnimationFrame
- [x] Input management (keyboard/mouse)
- [x] Asset loading system

### Game Engine
- [x] Sprite system
- [x] Animation system
- [x] Action sequences
- [ ] Collision detection
- [ ] Physics/movement system

### Game Logic
- [ ] Stage loading from JSON
- [ ] Unit system (Hero, Assistant)
- [ ] Skills system
- [ ] Game arena
- [ ] Score/points tracking
- [ ] Star system (bronze/silver/gold)

### Storage & Progression
- [ ] LocalStorage for player data
- [ ] Save/load game state
- [ ] Stage progression tracking

### UI/UX
- [ ] Main menu
- [ ] In-game HUD
- [ ] Toolbar/status bar
- [ ] Game over screen
- [ ] Stage selection

### Polish
- [ ] Sound effects
- [ ] Background music
- [ ] Particle effects
- [ ] Screen transitions
- [ ] Mobile responsive design

### Testing & QA
- [ ] Unit tests
- [ ] Integration tests
- [ ] E2E tests
- [ ] Performance optimization
- [ ] Browser compatibility

### Documentation
- [ ] Code documentation
- [ ] API documentation
- [ ] User guide
- [ ] Development guide

## Estimated Timeline

- **Phase 1-2 (Infrastructure)**: 1-2 weeks
- **Phase 3-4 (Core Systems)**: 2-3 weeks
- **Phase 5 (Game Logic)**: 3-4 weeks
- **Phase 6-7 (Utilities & Assets)**: 1-2 weeks
- **Phase 8 (UI)**: 1-2 weeks
- **Phase 9 (Testing)**: 1-2 weeks
- **Phase 10 (Polish & Deploy)**: 1 week

**Total Estimated Time**: 10-16 weeks

## Key Challenges & Solutions

### 1. JavaFX to Canvas Migration
**Challenge**: JavaFX provides a scene graph; Canvas is imperative
**Solution**: Implement Renderable pattern with manual render ordering

### 2. Threading Model
**Challenge**: Java uses Thread/Platform.runLater; JavaScript is single-threaded
**Solution**: Use async/await, Promises, and requestAnimationFrame

### 3. Property Binding
**Challenge**: JavaFX has built-in property binding
**Solution**: Implement observer pattern or use reactive library (RxJS)

### 4. Type Safety
**Challenge**: Converting Java generics to TypeScript
**Solution**: Leverage TypeScript's generic system and strict mode

### 5. Asset Loading
**Challenge**: JavaFX resources vs web assets
**Solution**: Implement async asset loader with caching

## Recommended Tools & Libraries

### Optional Enhancements
- **Pixi.js**: High-performance 2D WebGL renderer (alternative to Canvas)
- **Howler.js**: Audio library for sound effects and music
- **RxJS**: Reactive programming for complex state management
- **Zustand/Redux**: State management for complex game state
- **dat.GUI**: Debug UI for development

## Conclusion

This migration plan provides a comprehensive roadmap for converting Prishonor from Java/JavaFX to TypeScript/HTML5. The phased approach ensures systematic conversion of all game systems while maintaining the original gameplay experience. The use of modern web technologies will make the game more accessible and easier to distribute.
