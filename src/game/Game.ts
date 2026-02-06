import { RenderEngine as render_engine } from './RenderEngine';
import { GameLoop as game_loop } from './GameLoop';
import { KeyboardManager as keyboard_manager } from './keyboard-manager';
import { AssetLoader as asset_loader } from '@/assets/asset-loader';
import { StageRepository as stage_repository } from '@/storage/stage-repository';
import type { Stage as stage_type } from '@/types/stage-types';

export class Game {
  static #instance: Game;
  #render_engine: render_engine;
  #game_loop: game_loop;
  #keyboard_manager: keyboard_manager;
  #stage_repository: stage_repository;
  #stages: Map<number, stage_type> = new Map();
  #stage_count: number = 0;
  #is_initialized: boolean = false;
  #animation_time: number = 0;
  #last_input: string | null = null;

  private constructor() {
    this.#render_engine = new render_engine('gameCanvas', 600, 430);
    this.#keyboard_manager = new keyboard_manager();
    this.#stage_repository = new stage_repository(new asset_loader(), '/data/stages.json');
    this.#game_loop = new game_loop(
      delta_time => this.#update(delta_time),
      () => this.#render()
    );
    this.#setup_input_handlers();
  }

  static get_instance(): Game {
    if (!Game.#instance) Game.#instance = new Game();
    return Game.#instance;
  }

  async initialize(): Promise<void> {
    if (this.#is_initialized) return;

    await this.#load_stages();
    this.#draw_welcome_screen();
    this.#is_initialized = true;
    this.#game_loop.start();
  }

  #draw_welcome_screen(): void {
    const ctx = this.#render_engine.get_context();
    const canvas = this.#render_engine.get_canvas();
    ctx.fillStyle = '#0a0a0a';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    
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
    
    ctx.font = '12px Arial';
    ctx.fillStyle = '#4a9eff';
    ctx.fillText('Game loop is running...', canvas.width / 2, canvas.height / 2 + 80);
  }

  #update(delta_time: number): void {
    this.#render_engine.update(delta_time);
    this.#animate_welcome_screen(delta_time);
  }
  
  #animate_welcome_screen(delta_time: number): void {
    this.#animation_time += delta_time;
  }

  #render(): void {
    const ctx = this.#render_engine.get_context();
    const canvas = this.#render_engine.get_canvas();
    
    ctx.fillStyle = '#0a0a0a';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    
    this.#render_engine.render();
    
    const pulse = Math.sin(this.#animation_time * 2) * 0.3 + 0.7;
    
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
    
    ctx.globalAlpha = pulse;
    ctx.font = '12px Arial';
    ctx.fillStyle = '#4a9eff';
    ctx.fillText('● Game loop is running...', canvas.width / 2, canvas.height / 2 + 80);
    ctx.globalAlpha = 1;
    
    ctx.fillStyle = '#666';
    ctx.font = '10px monospace';
    ctx.textAlign = 'right';
    ctx.fillText(`FPS: 60`, canvas.width - 10, 20);
    ctx.fillText(`Stages: ${this.#stage_count}`, canvas.width - 10, 34);
    
    if (!this.#last_input) return;
    ctx.fillStyle = '#8fd3ff';
    ctx.textAlign = 'left';
    ctx.fillText(`Last input: ${this.#last_input}`, 10, 20);
  }

  pause(): void {
    this.#game_loop.pause();
  }

  resume(): void {
    this.#game_loop.resume();
  }

  stop(): void {
    this.#game_loop.stop();
    this.#keyboard_manager.dispose();
  }

  #setup_input_handlers(): void {
    this.#keyboard_manager.register_handler('ArrowUp', key_code => this.#set_last_input(key_code));
    this.#keyboard_manager.register_handler('ArrowDown', key_code => this.#set_last_input(key_code));
    this.#keyboard_manager.register_handler('ArrowLeft', key_code => this.#set_last_input(key_code));
    this.#keyboard_manager.register_handler('ArrowRight', key_code => this.#set_last_input(key_code));
    this.#keyboard_manager.register_handler('Space', key_code => this.#set_last_input(key_code));
    this.#keyboard_manager.register_handler('Enter', key_code => this.#set_last_input(key_code));
  }

  #set_last_input(key_code: string): void {
    if (!key_code) return;
    this.#last_input = key_code;
  }

  async #load_stages(): Promise<void> {
    this.#stages = await this.#stage_repository.load_all();
    this.#stage_count = this.#stages.size;
  }
}
