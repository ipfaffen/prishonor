import type { Position as position_type, Renderable as renderable_type, Size as size_type } from '@/types/GameTypes';
import type { SpriteImage as sprite_image } from './sprite-image';
import type { SpriteAnimation as sprite_animation } from './sprite-animation';

export class SpriteElement implements renderable_type {
  #sprite_image: sprite_image;
  #animation: sprite_animation;
  #position: position_type = {x:0,y:0};
  #size: size_type;
  #is_visible: boolean = true;

  constructor(sprite_image_instance: sprite_image, animation_instance: sprite_animation, size: size_type) {
    this.#sprite_image = sprite_image_instance;
    this.#animation = animation_instance;
    this.#size = {width:size.width,height:size.height};
  }

  set_position(position: position_type): void {
    this.#position = {x:position.x,y:position.y};
  }

  get_position(): position_type {
    return {x:this.#position.x,y:this.#position.y};
  }

  set_visible(is_visible: boolean): void {
    this.#is_visible = is_visible;
  }

  render(ctx: CanvasRenderingContext2D): void {
    if (!this.#is_visible) return;
    if (!this.#sprite_image.is_loaded()) return;

    const frame_index = this.#animation.get_current_frame_index();
    if (frame_index === null) return;

    const frame = this.#sprite_image.get_frame(frame_index);
    if (!frame) return;

    ctx.drawImage(
      this.#sprite_image.get_image(),
      frame.x,
      frame.y,
      frame.width,
      frame.height,
      this.#position.x,
      this.#position.y,
      this.#size.width,
      this.#size.height
    );
  }

  update(delta_time: number): void {
    this.#animation.update(delta_time);
  }
}
