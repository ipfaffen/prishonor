import { Game } from './game/Game';
import './style.css';

async function main() {
  try {
    // Create HTML structure
    createGameUI();

    // Show loading state
    showLoading();

    // Initialize and start game
    const game = Game.getInstance();
    await game.initialize();

    // Hide loading state
    hideLoading();
  } catch (error) {
    console.error('Failed to start game:', error);
    showError('Failed to load game. Please refresh the page.');
  }
}

function createGameUI(): void {
  const app = document.getElementById('app');
  if (!app) {
    throw new Error('App element not found');
  }
  
  app.innerHTML = `
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

function showLoading(): void {
  const loading = document.getElementById('loading');
  if (loading) {
    loading.style.display = 'block';
  }
}

function hideLoading(): void {
  const loading = document.getElementById('loading');
  const canvas = document.getElementById('gameCanvas');
  const footer = document.getElementById('gameFooter');
  
  if (loading) {
    loading.style.display = 'none';
  }
  
  if (canvas) {
    canvas.style.display = 'block';
  }
  
  if (footer) {
    footer.style.display = 'block';
  }
}

function showError(message: string): void {
  const app = document.getElementById('app');
  if (!app) return;
  
  app.innerHTML = `
    <div class="error-container">
      <h2>Error</h2>
      <p>${message}</p>
      <p style="margin-top: 20px; font-size: 0.9rem; opacity: 0.8;">
        Check the console for more details.
      </p>
    </div>
  `;
}

// Start the game when DOM is ready
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', main);
} else {
  main();
}
