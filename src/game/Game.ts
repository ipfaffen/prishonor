import { RenderEngine as render_engine } from './RenderEngine';
import { GameLoop as game_loop } from './GameLoop';
import { KeyboardManager as keyboard_manager } from './keyboard-manager';
import { StageNavigator as stage_navigator } from './stage-navigator';
import { StagePreviewRenderer as stage_preview_renderer } from './stage-preview-renderer';
import { AssetLoader as asset_loader } from '@/assets/asset-loader';
import { StageRepository as stage_repository } from '@/storage/stage-repository';
import { PlayerStorage as player_storage } from '@/storage/player-storage';
import type { Stage as stage_type } from '@/types/stage-types';
import type { PlayerProfile as player_profile } from '@/types/player-types';

export class Game {
  static #instance: Game;
  #render_engine: render_engine;
  #game_loop: game_loop;
  #keyboard_manager: keyboard_manager;
  #stage_repository: stage_repository;
  #stages: Map<number, stage_type> = new Map();
  #stage_count: number = 0;
  #stage_navigator: stage_navigator | null = null;
  #stage_preview_renderer: stage_preview_renderer;
  #player_storage: player_storage;
  #player_profile: player_profile;
  #is_initialized: boolean = false;
  #animation_time: number = 0;
  #last_input: string | null = null;

  private constructor() {
    this.#render_engine = new render_engine('gameCanvas', 600, 430);
    this.#keyboard_manager = new keyboard_manager();
    this.#stage_repository = new stage_repository(new asset_loader(), '/data/stages.json');
    this.#player_storage = new player_storage('prishonor_player');
    this.#player_profile = this.#player_storage.load();
    this.#stage_preview_renderer = new stage_preview_renderer(32);
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
    this.#stage_navigator = new stage_navigator(this.#stages, this.#player_profile.last_played_stage_id);
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
    this.#stage_preview_renderer.render(ctx, this.#get_current_stage());
    
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
    
    ctx.fillStyle = '#8fd3ff';
    ctx.textAlign = 'left';
    ctx.fillText(`Player stage: ${this.#get_current_stage_id()}`, 10, 34);
    const stage_label = this.#get_stage_label();
    if (stage_label) ctx.fillText(stage_label, 10, 48);
    if (!this.#last_input) return;
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
    this.#save_player_profile();
  }

  #setup_input_handlers(): void {
    this.#keyboard_manager.register_handler('ArrowUp', key_code => this.#set_last_input(key_code));
    this.#keyboard_manager.register_handler('ArrowDown', key_code => this.#set_last_input(key_code));
    this.#keyboard_manager.register_handler('ArrowLeft', key_code => this.#set_last_input(key_code));
    this.#keyboard_manager.register_handler('ArrowRight', key_code => this.#set_last_input(key_code));
    this.#keyboard_manager.register_handler('Space', key_code => this.#set_last_input(key_code));
    this.#keyboard_manager.register_handler('Enter', key_code => this.#set_last_input(key_code));
    this.#keyboard_manager.register_handler('Tab', key_code => this.#advance_stage(key_code));
  }

  #set_last_input(key_code: string): void {
    if (!key_code) return;
    this.#last_input = key_code;
  }

  async #load_stages(): Promise<void> {
    this.#stages = await this.#stage_repository.load_all();
    this.#stage_count = this.#stages.size;
  }

  #get_current_stage_id(): number {
    if (!this.#stage_navigator) return this.#player_profile.last_played_stage_id;
    return this.#stage_navigator.get_current_stage_id();
  }

  #get_current_stage(): stage_type | null {
    if (!this.#stage_navigator) return null;
    return this.#stage_navigator.get_current_stage();
  }

  #save_player_profile(): void {
    this.#player_profile.last_played_stage_id = this.#get_current_stage_id();
    this.#player_storage.save(this.#player_profile);
  }

  #advance_stage(key_code: string): void {
    this.#set_last_input(key_code);
    if (!this.#stage_navigator) return;

    this.#stage_navigator.next_stage();
    this.#save_player_profile();
  }

  #get_stage_label(): string | null {
    if (!this.#stage_navigator) return null;

    const stage = this.#stage_navigator.get_current_stage();
    if (!stage) return null;

    return `Stage ${stage.id}: ${stage.background_image_name}`;
  }
}
