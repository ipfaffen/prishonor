import type { Renderable } from '@/types/GameTypes';

export class RenderEngine {
  private canvas: HTMLCanvasElement;
  private ctx: CanvasRenderingContext2D;
  private renderables: Renderable[] = [];

  constructor(canvasId: string, width: number, height: number) {
    const canvas = document.getElementById(canvasId) as HTMLCanvasElement;
    if (!canvas) {
      throw new Error(`Canvas with id "${canvasId}" not found`);
    }
    
    this.canvas = canvas;
    this.canvas.width = width;
    this.canvas.height = height;
    
    const ctx = this.canvas.getContext('2d');
    if (!ctx) {
      throw new Error('Could not get 2D rendering context');
    }
    
    this.ctx = ctx;
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

  clear(): void {
    this.renderables = [];
  }

  getContext(): CanvasRenderingContext2D {
    return this.ctx;
  }

  getCanvas(): HTMLCanvasElement {
    return this.canvas;
  }

  render(): void {
    // Clear canvas
    this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

    // Render all renderables
    this.renderables.forEach(renderable => {
      renderable.render(this.ctx);
    });
  }

  update(deltaTime: number): void {
    this.renderables.forEach(renderable => {
      renderable.update(deltaTime);
    });
  }
}
