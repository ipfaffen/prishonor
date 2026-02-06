import type { Action as action_type } from './action-types';

export class ActionSequence {
  #actions: action_type[];
  #current_index: number = 0;
  #is_playing: boolean = false;
  #on_complete: (() => void) | null = null;

  constructor(actions: action_type[]) {
    this.#actions = actions;
  }

  play(on_complete: (() => void) | null = null): void {
    this.#is_playing = true;
    this.#current_index = 0;
    this.#on_complete = on_complete;
  }

  stop(): void {
    this.#is_playing = false;
    this.#current_index = 0;
    this.#actions.forEach(action => action.reset());
  }

  update(delta_time: number): void {
    if (!this.#is_playing) return;
    if (this.#current_index >= this.#actions.length) return;

    const current_action = this.#actions[this.#current_index];
    const is_complete = current_action.execute(delta_time);

    if (!is_complete) return;

    this.#current_index += 1;

    if (this.#current_index < this.#actions.length) return;

    this.#is_playing = false;
    if (!this.#on_complete) return;
    this.#on_complete();
  }
}
