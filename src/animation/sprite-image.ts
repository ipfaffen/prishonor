import type { SpriteFrame as sprite_frame } from './sprite-types';

type sprite_grid_config = {
  cell_width: number;
  cell_height: number;
  offset_x: number;
  offset_y: number;
};

export class SpriteImage {
  #image: HTMLImageElement;
  #frames: sprite_frame[];
  #is_loaded: boolean = false;
  #grid_config: sprite_grid_config | null;
  #rows: number = 0;
  #columns: number = 0;

  constructor(image_path: string, frames: sprite_frame[], grid_config: sprite_grid_config | null=null) {
    this.#image = new Image();
    this.#frames = frames;
    this.#grid_config = grid_config;
    this.#image.onload = () => {
      this.#is_loaded = true;
      if (this.#grid_config) this.#frames = this.#build_grid_frames(this.#grid_config);
    };
    this.#image.src = image_path;
  }

  static create_from_grid(image_path: string, grid_config: sprite_grid_config): SpriteImage {
    return new SpriteImage(image_path, [], grid_config);
  }

  is_loaded(): boolean {
    return this.#is_loaded;
  }

  get_image(): HTMLImageElement {
    return this.#image;
  }

  get_frame(index: number): sprite_frame | null {
    if (!Number.isFinite(index)) return null;
    return this.#frames[index] ?? null;
  }

  get_frame_count(): number {
    return this.#frames.length;
  }

  get_rows(): number {
    return this.#rows;
  }

  get_columns(): number {
    return this.#columns;
  }

  #build_grid_frames(grid_config: sprite_grid_config): sprite_frame[] {
    const total_columns = Math.floor(this.#image.width / (grid_config.cell_width + grid_config.offset_x));
    const total_rows = Math.floor(this.#image.height / (grid_config.cell_height + grid_config.offset_y));
    this.#columns = total_columns;
    this.#rows = total_rows;

    const frames: sprite_frame[] = [];

    for (let row = 0; row < total_rows; row += 1) {
      for (let col = 0; col < total_columns; col += 1) {
        frames.push({
          x:grid_config.offset_x + col * (grid_config.cell_width + grid_config.offset_x),
          y:grid_config.offset_y + row * (grid_config.cell_height + grid_config.offset_y),
          width:grid_config.cell_width,
          height:grid_config.cell_height
        });
      }
    }

    return frames;
  }
}
