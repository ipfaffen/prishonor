import type { Position as position_type, Renderable as renderable_type, Size as size_type } from '@/types/GameTypes';
import { SpriteImage as sprite_image } from '@/animation/sprite-image';
import { SpriteAnimation as sprite_animation } from '@/animation/sprite-animation';
import { SpriteElement as sprite_element } from '@/animation/sprite-element';

export class HeroPreview implements renderable_type {
  #sprite_image: sprite_image;
  #sprite_animation: sprite_animation | null = null;
  #sprite_element: sprite_element | null = null;
  #position: position_type;
  #size: size_type;

  constructor(image_path: string, position: position_type, size: size_type) {
    this.#sprite_image = sprite_image.create_from_grid(image_path, {
      cell_width:24,
      cell_height:24,
      offset_x:1,
      offset_y:1
    });
    this.#position = {x:position.x,y:position.y};
    this.#size = {width:size.width,height:size.height};
  }

  render(ctx: CanvasRenderingContext2D): void {
    if (!this.#sprite_element) return;
    this.#sprite_element.render(ctx);
  }

  update(delta_time: number): void {
    if (!this.#sprite_image.is_loaded()) return;
    if (!this.#sprite_animation || !this.#sprite_element) this.#initialize_sprite();
    if (!this.#sprite_animation || !this.#sprite_element) return;
    this.#sprite_element.update(delta_time);
  }

  #initialize_sprite(): void {
    if (this.#sprite_animation || this.#sprite_element) return;

    const frame_sequence = this.#build_frame_sequence(0, 3, 8);
    this.#sprite_animation = new sprite_animation(this.#sprite_image, frame_sequence, 0.12);
    this.#sprite_animation.set_loop(true);
    this.#sprite_animation.play();

    this.#sprite_element = new sprite_element(this.#sprite_image, this.#sprite_animation, this.#size);
    this.#sprite_element.set_position(this.#position);
  }

  #build_frame_sequence(row: number, start_col: number, end_col: number): number[] {
    const columns = this.#sprite_image.get_columns();
    if (columns <= 0) return [];

    const safe_start = Math.max(0, start_col);
    const safe_end = Math.max(safe_start, end_col);
    const frame_sequence: number[] = [];

    for (let col = safe_start; col <= safe_end; col += 1) {
      frame_sequence.push(row * columns + col);
    }

    return frame_sequence;
  }
}
