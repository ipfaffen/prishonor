import type { Position as position_type } from '@/types/GameTypes';

export interface ActionTarget {
  set_position(position: position_type): void;
}

export interface Action {
  execute(delta_time: number): boolean;
  reset(): void;
}
