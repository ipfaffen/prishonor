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
    const raw_stages = await this.#asset_loader.load_json<unknown>(this.#stages_path);
    if (!Array.isArray(raw_stages)) return new Map();

    const stages = new Map<number, stage_type>();
    raw_stages.forEach(stage_item => {
      const normalized = this.#normalize_stage(stage_item);
      if (!normalized) return;
      stages.set(normalized.id, normalized);
    });

    return stages;
  }

  #normalize_stage(stage_item: unknown): stage_type | null {
    if (!stage_item || typeof stage_item !== 'object') return null;
    const typed_stage = stage_item as stage_type;
    if (Number.isFinite(typed_stage.id) && Array.isArray(typed_stage.stage_objects)) return typed_stage;

    const legacy_stage = stage_item as {
      id?: number;
      bronzeMedalPoints?: number;
      silverMedalPoints?: number;
      goldMedalPoints?: number;
      background?: {imageName?: string};
      stageObjects?: Array<{
        positionX?: number;
        positionY?: number;
        background?: {imageName?: string};
      }>;
    };

    if (!Number.isFinite(legacy_stage.id)) return null;
    if (!Array.isArray(legacy_stage.stageObjects)) return null;

    return {
      id: legacy_stage.id,
      background_image_name: legacy_stage.background?.imageName ?? '',
      stage_objects: legacy_stage.stageObjects
        .filter(stage_object => Number.isFinite(stage_object.positionX) && Number.isFinite(stage_object.positionY))
        .map(stage_object => ({
          x: stage_object.positionX as number,
          y: stage_object.positionY as number,
          image_name: stage_object.background?.imageName ?? ''
        })),
      bronze_medal_points: legacy_stage.bronzeMedalPoints ?? 0,
      silver_medal_points: legacy_stage.silverMedalPoints ?? 0,
      gold_medal_points: legacy_stage.goldMedalPoints ?? 0
    };
  }
}
