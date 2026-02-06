import type { PlayerProfile as player_profile } from '@/types/player-types';
import type { Stage as stage_type } from '@/types/stage-types';
import type { StageProgressSummary as stage_progress_summary } from '@/types/player-progress-types';

export class PlayerProgress {
  #profile: player_profile;

  constructor(profile: player_profile) {
    this.#profile = profile;
  }

  get_stage_progress(stage: stage_type | null): stage_progress_summary | null {
    if (!stage) return null;

    const best_score = this.#get_stage_score(stage.id);
    const medal_label = this.#resolve_medal_label(stage, best_score);
    const next_target = this.#resolve_next_target(stage, best_score);

    return {
      best_score,
      medal_label,
      next_target
    };
  }

  #get_stage_score(stage_id: number): number | null {
    const stage_score = this.#profile.stage_scores.find(score => score.stage_id === stage_id);
    if (!stage_score) return null;
    if (!Number.isFinite(stage_score.score)) return null;
    return stage_score.score;
  }

  #resolve_medal_label(stage: stage_type, score: number | null): string {
    if (score === null) return 'none';
    if (score >= stage.gold_medal_points) return 'gold';
    if (score >= stage.silver_medal_points) return 'silver';
    if (score >= stage.bronze_medal_points) return 'bronze';
    return 'none';
  }

  #resolve_next_target(stage: stage_type, score: number | null): number | null {
    if (score === null) return stage.bronze_medal_points;
    if (score < stage.bronze_medal_points) return stage.bronze_medal_points;
    if (score < stage.silver_medal_points) return stage.silver_medal_points;
    if (score < stage.gold_medal_points) return stage.gold_medal_points;
    return null;
  }
}
