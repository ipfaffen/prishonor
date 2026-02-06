import type { Stage as stage_type } from '@/types/stage-types';
import { AssetLoader as asset_loader } from '@/assets/asset-loader';

export class StageRepository {
  #asset_loader: asset_loader;
  #stages_path: string;

  constructor(asset_loader_instance: asset_loader, stages_path: string) {
    this.#asset_loader = asset_loader_instance;
    this.#stages_path = stages_path;
  }

  async load_all(): Promise<Map<number, stage_type>> {
    const raw_stages = await this.#asset_loader.load_json<stage_type[]>(this.#stages_path);
    if (!Array.isArray(raw_stages)) return new Map();

    const stages = new Map<number, stage_type>();
    raw_stages.forEach(stage_item => {
      if (!Number.isFinite(stage_item.id)) return;
      stages.set(stage_item.id, stage_item);
    });

    return stages;
  }
}
