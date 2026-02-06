import type { Position as position_type } from '@/types/GameTypes';
import type { Action as action_type, ActionTarget as action_target } from './action-types';

export class MoveAction implements action_type {
  #target: action_target;
  #start_position: position_type;
  #end_position: position_type;
  #duration: number;
  #elapsed: number = 0;

  constructor(target: action_target, end_position: position_type, duration: number) {
    this.#target = target;
    this.#start_position = {x:0,y:0};
    this.#end_position = {x:end_position.x,y:end_position.y};
    this.#duration = duration;
  }

  set_start_position(start_position: position_type): void {
    this.#start_position = {x:start_position.x,y:start_position.y};
    this.#elapsed = 0;
  }

  execute(delta_time: number): boolean {
    if (this.#duration <= 0) {
      this.#target.set_position({x:this.#end_position.x,y:this.#end_position.y});
      return true;
    }

    this.#elapsed += delta_time;
    const progress = Math.min(this.#elapsed / this.#duration, 1);
    const next_position = {
      x:this.#start_position.x + (this.#end_position.x - this.#start_position.x) * progress,
      y:this.#start_position.y + (this.#end_position.y - this.#start_position.y) * progress
    };

    this.#target.set_position(next_position);
    return progress >= 1;
  }

  reset(): void {
    this.#elapsed = 0;
  }
}
