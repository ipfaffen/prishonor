import type { SpriteImage as sprite_image } from './sprite-image';

export class SpriteAnimation {
  #sprite_image: sprite_image;
  #frame_sequence: number[];
  #frame_duration: number;
  #current_frame: number = 0;
  #elapsed_time: number = 0;
  #is_playing: boolean = false;
  #is_looping: boolean = true;

  constructor(sprite_image_instance: sprite_image, frame_sequence: number[], frame_duration: number) {
    this.#sprite_image = sprite_image_instance;
    this.#frame_sequence = frame_sequence;
    this.#frame_duration = frame_duration;
  }

  play(): void {
    this.#is_playing = true;
  }

  pause(): void {
    this.#is_playing = false;
  }

  stop(): void {
    this.#is_playing = false;
    this.#current_frame = 0;
    this.#elapsed_time = 0;
  }

  set_loop(is_looping: boolean): void {
    this.#is_looping = is_looping;
  }

  update(delta_time: number): void {
    if (!this.#is_playing) return;
    if (this.#frame_duration <= 0) return;

    this.#elapsed_time += delta_time;

    if (this.#elapsed_time < this.#frame_duration) return;

    this.#elapsed_time = 0;
    this.#current_frame += 1;

    if (this.#current_frame < this.#frame_sequence.length) return;

    if (this.#is_looping) {
      this.#current_frame = 0;
      return;
    }

    this.#current_frame = Math.max(0, this.#frame_sequence.length - 1);
    this.#is_playing = false;
  }

  get_current_frame_index(): number | null {
    if (this.#frame_sequence.length === 0) return null;
    return this.#frame_sequence[this.#current_frame] ?? null;
  }

  get_sprite_image(): sprite_image {
    return this.#sprite_image;
  }

  is_playing(): boolean {
    return this.#is_playing;
  }
}
