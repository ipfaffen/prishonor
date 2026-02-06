export class GameLoop {
  #is_running: boolean = false;
  #is_paused: boolean = false;
  #last_frame_time: number = 0;
  #fps: number = 60;
  #frame_interval: number = 1000 / this.#fps;
  #animation_frame_id: number | null = null;
  #update_callback: (delta_time: number) => void;
  #render_callback: () => void;

  constructor(
    update_callback: (delta_time: number) => void,
    render_callback: () => void
  ) {
    this.#update_callback = update_callback;
    this.#render_callback = render_callback;
  }

  start(): void {
    if (this.#is_running) return;
    
    this.#is_running = true;
    this.#last_frame_time = performance.now();
    this.#loop(this.#last_frame_time);
  }

  pause(): void {
    this.#is_paused = true;
  }

  resume(): void {
    if (this.#is_paused) {
      this.#is_paused = false;
      this.#last_frame_time = performance.now();
    }
  }

  stop(): void {
    this.#is_running = false;
    if (this.#animation_frame_id === null) return;
    
    cancelAnimationFrame(this.#animation_frame_id);
    this.#animation_frame_id = null;
  }

  get_is_running(): boolean {
    return this.#is_running;
  }

  get_is_paused(): boolean {
    return this.#is_paused;
  }

  #loop(current_time: number): void {
    if (!this.#is_running) return;

    const elapsed = current_time - this.#last_frame_time;

    if (elapsed >= this.#frame_interval) {
      const delta_time = elapsed / 1000;
      
      if (!this.#is_paused) this.#update_callback(delta_time);
      
      this.#render_callback();
      this.#last_frame_time = current_time - (elapsed % this.#frame_interval);
    }

    this.#animation_frame_id = requestAnimationFrame(time => this.#loop(time));
  }
}
