import type { Stage as stage_type } from '@/types/stage-types';

export class StageNavigator {
  #stages: Map<number, stage_type>;
  #ordered_ids: number[];
  #current_stage_id: number;

  constructor(stages: Map<number, stage_type>, initial_stage_id: number) {
    this.#stages = stages;
    this.#ordered_ids = Array.from(stages.keys()).sort((left_id, right_id) => left_id - right_id);
    this.#current_stage_id = this.#resolve_initial_stage_id(initial_stage_id);
  }

  get_current_stage_id(): number {
    return this.#current_stage_id;
  }

  get_current_stage(): stage_type | null {
    return this.#stages.get(this.#current_stage_id) ?? null;
  }

  has_stages(): boolean {
    return this.#ordered_ids.length > 0;
  }

  next_stage(): number {
    if (!this.has_stages()) return this.#current_stage_id;

    const index = this.#ordered_ids.indexOf(this.#current_stage_id);
    const next_index = index === -1 ? 0 : (index + 1) % this.#ordered_ids.length;
    this.#current_stage_id = this.#ordered_ids[next_index];
    return this.#current_stage_id;
  }

  previous_stage(): number {
    if (!this.has_stages()) return this.#current_stage_id;

    const index = this.#ordered_ids.indexOf(this.#current_stage_id);
    const previous_index = index <= 0 ? this.#ordered_ids.length - 1 : index - 1;
    this.#current_stage_id = this.#ordered_ids[previous_index];
    return this.#current_stage_id;
  }

  #resolve_initial_stage_id(initial_stage_id: number): number {
    if (this.#stages.has(initial_stage_id)) return initial_stage_id;
    if (this.#ordered_ids.length === 0) return initial_stage_id;
    return this.#ordered_ids[0];
  }
}
