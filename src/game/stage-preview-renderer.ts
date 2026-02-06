import type { Stage as stage_type } from '@/types/stage-types';

export class StagePreviewRenderer {
  #tile_size: number;

  constructor(tile_size: number) {
    this.#tile_size = tile_size;
  }

  render(ctx: CanvasRenderingContext2D, stage: stage_type | null): void {
    if (!stage) return;

    ctx.save();
    ctx.fillStyle = 'rgba(255, 255, 255, 0.08)';
    stage.stage_objects.forEach(stage_object => {
      ctx.fillRect(
        stage_object.x,
        stage_object.y,
        this.#tile_size,
        this.#tile_size
      );
    });
    ctx.restore();
  }
}
