import type { Stage as stage_type } from '@/types/stage-types';
import { AssetLoader as asset_loader } from '@/assets/asset-loader';

export class StageBackgroundRenderer {
  #asset_loader: asset_loader;

  constructor(asset_loader_instance: asset_loader) {
    this.#asset_loader = asset_loader_instance;
  }

  render(ctx: CanvasRenderingContext2D, stage: stage_type | null, canvas: HTMLCanvasElement): void {
    if (!stage) return;
    if (!stage.background_image_name) return;

    const image_path = this.#get_background_path(stage.background_image_name);
    const image = this.#asset_loader.get_cached_image(image_path);

    if (!image) {
      void this.#asset_loader.load_image(image_path).catch(() => null);
      return;
    }

    ctx.drawImage(image, 0, 0, canvas.width, canvas.height);
  }

  #get_background_path(image_name: string): string {
    if (!image_name) return '';
    return `/images/stage/${image_name}`;
  }
}
