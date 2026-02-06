export interface PlayerStageScore {
  stage_id: number;
  score: number;
}

export interface PlayerProfile {
  last_played_stage_id: number;
  stage_scores: PlayerStageScore[];
}
