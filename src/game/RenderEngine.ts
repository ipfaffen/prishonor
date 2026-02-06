import type { Renderable as renderable_type } from '@/types/GameTypes';

export class RenderEngine {
  #canvas: HTMLCanvasElement;
  #ctx: CanvasRenderingContext2D;
  #renderables: renderable_type[] = [];

  constructor(canvas_id: string, width: number, height: number) {
    const canvas = document.getElementById(canvas_id) as HTMLCanvasElement | null;
    if (!canvas) throw new Error(`Canvas with id "${canvas_id}" not found`);
    
    this.#canvas = canvas;
    this.#canvas.width = width;
    this.#canvas.height = height;
    
    const ctx = this.#canvas.getContext('2d');
    if (!ctx) throw new Error('Could not get 2D rendering context');
    
    this.#ctx = ctx;
  }

  add(renderable: renderable_type): void {
    this.#renderables.push(renderable);
  }

  remove(renderable: renderable_type): void {
    const index = this.#renderables.indexOf(renderable);
    if (index > -1) {
      this.#renderables.splice(index, 1);
    }
  }

  clear(): void {
    this.#renderables = [];
  }

  get_context(): CanvasRenderingContext2D {
    return this.#ctx;
  }

  get_canvas(): HTMLCanvasElement {
    return this.#canvas;
  }

  render(): void {
    this.#ctx.clearRect(0, 0, this.#canvas.width, this.#canvas.height);

    this.#renderables.forEach(renderable => {
      renderable.render(this.#ctx);
    });
  }

  update(delta_time: number): void {
    this.#renderables.forEach(renderable => {
      renderable.update(delta_time);
    });
  }
}
