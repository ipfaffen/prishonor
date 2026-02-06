export class GameLoop {
  private isRunning: boolean = false;
  private isPaused: boolean = false;
  private lastFrameTime: number = 0;
  private fps: number = 60;
  private frameInterval: number = 1000 / this.fps;
  private animationFrameId: number | null = null;

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
    if (this.isPaused) {
      this.isPaused = false;
      this.lastFrameTime = performance.now();
    }
  }

  stop(): void {
    this.isRunning = false;
    if (this.animationFrameId !== null) {
      cancelAnimationFrame(this.animationFrameId);
      this.animationFrameId = null;
    }
  }

  getIsRunning(): boolean {
    return this.isRunning;
  }

  getIsPaused(): boolean {
    return this.isPaused;
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

    this.animationFrameId = requestAnimationFrame((time) => this.loop(time));
  }
}
