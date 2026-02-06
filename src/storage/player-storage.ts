import type { PlayerProfile as player_profile } from '@/types/player-types';

export class PlayerStorage {
  #player_key: string;

  constructor(player_key: string) {
    this.#player_key = player_key;
  }

  load(): player_profile {
    const raw_data = localStorage.getItem(this.#player_key);
    if (!raw_data) return this.#create_default();

    const parsed = this.#safe_parse(raw_data);
    if (!parsed) return this.#create_default();
    if (!Number.isFinite(parsed.last_played_stage_id)) return this.#create_default();
    if (!Array.isArray(parsed.stage_scores)) return this.#create_default();

    return {
      last_played_stage_id: parsed.last_played_stage_id,
      stage_scores: parsed.stage_scores
    };
  }

  save(profile: player_profile): void {
    const payload = JSON.stringify(profile);
    localStorage.setItem(this.#player_key, payload);
  }

  #create_default(): player_profile {
    return {
      last_played_stage_id: 1,
      stage_scores: []
    };
  }

  #safe_parse(raw_data: string): player_profile | null {
    try {
      return JSON.parse(raw_data) as player_profile;
    } catch {
      return null;
    }
  }
}
