import { Game as game } from './game/Game';
import './style.css';

async function main(): Promise<void> {
  const app_container = get_app_container();
  render_game_ui(app_container);
  show_loading();

  const game_instance = game.get_instance();
  await game_instance.initialize();
  hide_loading();
}

function render_game_ui(app_container: HTMLElement): void {
  
  app_container.innerHTML = `
    <div class="game-container">
      <div class="game-header">
        <h1>Prishonor</h1>
        <p>A sprite-based puzzle adventure game</p>
      </div>
      <div class="loading-container" id="loading">
        <h2>Loading...</h2>
        <div class="spinner"></div>
      </div>
      <canvas id="gameCanvas" style="display: none;"></canvas>
      <div class="game-footer" style="display: none;" id="gameFooter">
        <div class="controls">
          <p>
            <strong>Arrow Keys:</strong> Move | 
            <strong>Space:</strong> Use Skill | 
            <strong>Tab:</strong> Change Skill | 
            <strong>Enter:</strong> Restart
          </p>
        </div>
      </div>
    </div>
  `;
}

function show_loading(): void {
  const loading = document.getElementById('loading');
  if (loading) loading.style.display = 'block';
}

function hide_loading(): void {
  const loading = document.getElementById('loading');
  const canvas = document.getElementById('gameCanvas');
  const footer = document.getElementById('gameFooter');
  
  if (loading) loading.style.display = 'none';
  if (canvas) canvas.style.display = 'block';
  if (footer) footer.style.display = 'block';
}

function get_app_container(): HTMLElement {
  const app_container = document.getElementById('app');
  if (app_container) return app_container;

  throw new Error('App element not found');
}

const boot = (): void => {
  main().catch(() => show_boot_error());
};

function show_boot_error(): void {
  const app_container = document.getElementById('app');
  if (!app_container) return;
  
  app_container.innerHTML = `
    <div class="error-container">
      <h2>Error</h2>
      <p>Failed to load game. Please refresh the page.</p>
      <p style="margin-top: 20px; font-size: 0.9rem; opacity: 0.8;">
        Check the console for more details.
      </p>
    </div>
  `;
}

if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', boot);
else boot();
