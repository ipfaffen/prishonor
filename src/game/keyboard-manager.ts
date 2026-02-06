export type key_handler = (key_code: string) => void;

export class KeyboardManager {
  #key_state: Map<string, boolean> = new Map();
  #key_handlers: Map<string, key_handler> = new Map();
  #handle_key_down_bound: (event: KeyboardEvent) => void;
  #handle_key_up_bound: (event: KeyboardEvent) => void;

  constructor() {
    this.#handle_key_down_bound = event => this.#handle_key_down(event);
    this.#handle_key_up_bound = event => this.#handle_key_up(event);
    this.#setup_listeners();
  }

  register_handler(key_code: string, handler: key_handler): void {
    this.#key_handlers.set(key_code, handler);
  }

  unregister_handler(key_code: string): void {
    this.#key_handlers.delete(key_code);
  }

  is_pressed(key_code: string): boolean {
    return this.#key_state.get(key_code) ?? false;
  }

  dispose(): void {
    window.removeEventListener('keydown', this.#handle_key_down_bound);
    window.removeEventListener('keyup', this.#handle_key_up_bound);
    this.#key_state.clear();
    this.#key_handlers.clear();
  }

  #handle_key_down(event: KeyboardEvent): void {
    event.preventDefault();
    const key_code = event.code;
    if (this.#key_state.get(key_code)) return;

    this.#key_state.set(key_code, true);
    const handler = this.#key_handlers.get(key_code);
    if (handler) handler(key_code);
  }

  #handle_key_up(event: KeyboardEvent): void {
    this.#key_state.set(event.code, false);
  }

  #setup_listeners(): void {
    window.addEventListener('keydown', this.#handle_key_down_bound);
    window.addEventListener('keyup', this.#handle_key_up_bound);
  }
}
