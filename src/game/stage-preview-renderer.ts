import type { Stage as stage_type } from '@/types/stage-types';
import { AssetLoader as asset_loader } from '@/assets/asset-loader';

export class StagePreviewRenderer {
  #tile_size: number;
  #asset_loader: asset_loader;

  constructor(asset_loader_instance: asset_loader, tile_size: number) {
    this.#asset_loader = asset_loader_instance;
    this.#tile_size = tile_size;
  }

  render(ctx: CanvasRenderingContext2D, stage: stage_type | null): void {
    if (!stage) return;

    ctx.save();
    stage.stage_objects.forEach(stage_object => {
      if (!stage_object.image_name) return;
      const image_path = this.#get_stage_image_path(stage_object.image_name);
      const image = this.#asset_loader.get_cached_image(image_path);
      if (!image) {
        void this.#asset_loader.load_image(image_path).catch(() => null);
        return;
      }
      ctx.drawImage(
        image,
        stage_object.x,
        stage_object.y,
        this.#tile_size,
        this.#tile_size
      );
    });
    ctx.restore();
  }

  #get_stage_image_path(image_name: string): string {
    if (!image_name) return '';
    return `/images/stage/${image_name}`;
  }
}
