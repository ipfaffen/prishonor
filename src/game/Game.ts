import { RenderEngine } from './RenderEngine';
import { GameLoop } from './GameLoop';
import type { Renderable } from '@/types/GameTypes';

export class Game {
  private static instance: Game;
  
  private renderEngine: RenderEngine;
  private gameLoop: GameLoop;
  private isInitialized: boolean = false;

  private constructor() {
    // Initialize render engine with game canvas
    this.renderEngine = new RenderEngine('gameCanvas', 600, 430);
    
    // Initialize game loop
    this.gameLoop = new GameLoop(
      (deltaTime) => this.update(deltaTime),
      () => this.render()
    );
  }

  static getInstance(): Game {
    if (!Game.instance) {
      Game.instance = new Game();
    }
    return Game.instance;
  }

  async initialize(): Promise<void> {
    if (this.isInitialized) {
      console.warn('Game already initialized');
      return;
    }

    try {
      console.log('Initializing game...');
      
      // For Phase 1, just display a welcome message on the canvas
      this.drawWelcomeScreen();
      
      this.isInitialized = true;
      console.log('Game initialized successfully');
      
      // Start the game loop
      this.gameLoop.start();
    } catch (error) {
      console.error('Failed to initialize game:', error);
      throw error;
    }
  }

  private drawWelcomeScreen(): void {
    const ctx = this.renderEngine.getContext();
    const canvas = this.renderEngine.getCanvas();
    
    // Fill background
    ctx.fillStyle = '#0a0a0a';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    
    // Draw welcome text
    ctx.fillStyle = '#4a9eff';
    ctx.font = 'bold 32px Arial';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.fillText('Prishonor', canvas.width / 2, canvas.height / 2 - 40);
    
    // Draw subtitle
    ctx.fillStyle = '#b9b9b9';
    ctx.font = '18px Arial';
    ctx.fillText('TypeScript Migration - Phase 1', canvas.width / 2, canvas.height / 2);
    
    // Draw info text
    ctx.font = '14px Arial';
    ctx.fillText('Infrastructure Setup Complete', canvas.width / 2, canvas.height / 2 + 40);
    
    // Draw animation indicator
    ctx.font = '12px Arial';
    ctx.fillStyle = '#4a9eff';
    ctx.fillText('Game loop is running...', canvas.width / 2, canvas.height / 2 + 80);
  }

  private update(deltaTime: number): void {
    // Update game entities
    this.renderEngine.update(deltaTime);
    
    // For Phase 1, we'll just update a simple animation
    this.animateWelcomeScreen(deltaTime);
  }

  private animationTime: number = 0;
  
  private animateWelcomeScreen(deltaTime: number): void {
    this.animationTime += deltaTime;
  }

  private render(): void {
    const ctx = this.renderEngine.getContext();
    const canvas = this.renderEngine.getCanvas();
    
    // Clear and redraw
    ctx.fillStyle = '#0a0a0a';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    
    // Render all game entities
    this.renderEngine.render();
    
    // Draw welcome screen with pulsing animation
    const pulse = Math.sin(this.animationTime * 2) * 0.3 + 0.7;
    
    ctx.fillStyle = '#4a9eff';
    ctx.font = 'bold 32px Arial';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.fillText('Prishonor', canvas.width / 2, canvas.height / 2 - 40);
    
    ctx.fillStyle = '#b9b9b9';
    ctx.font = '18px Arial';
    ctx.fillText('TypeScript Migration - Phase 1', canvas.width / 2, canvas.height / 2);
    
    ctx.font = '14px Arial';
    ctx.fillText('Infrastructure Setup Complete', canvas.width / 2, canvas.height / 2 + 40);
    
    // Pulsing indicator
    ctx.globalAlpha = pulse;
    ctx.font = '12px Arial';
    ctx.fillStyle = '#4a9eff';
    ctx.fillText('● Game loop is running...', canvas.width / 2, canvas.height / 2 + 80);
    ctx.globalAlpha = 1;
    
    // FPS counter
    ctx.fillStyle = '#666';
    ctx.font = '10px monospace';
    ctx.textAlign = 'right';
    ctx.fillText(`FPS: 60`, canvas.width - 10, 20);
  }

  pause(): void {
    this.gameLoop.pause();
  }

  resume(): void {
    this.gameLoop.resume();
  }

  stop(): void {
    this.gameLoop.stop();
  }
}
