import type { Stage as stage_type } from '@/types/stage-types';
import type { StageProgressSummary as stage_progress_summary } from '@/types/player-progress-types';

export class HudRenderer {
  #padding: number;
  #line_height: number;

  constructor(padding=12, line_height=14) {
    this.#padding = padding;
    this.#line_height = line_height;
  }

  render(
    ctx: CanvasRenderingContext2D,
    stage: stage_type | null,
    stage_count: number,
    current_stage_id: number,
    last_input: string | null,
    progress_summary: stage_progress_summary | null
  ): void {
    ctx.save();
    ctx.textAlign = 'left';
    ctx.textBaseline = 'top';
    ctx.font = '12px monospace';

    let cursor_y = this.#padding;

    this.#render_line(ctx, `Stage: ${current_stage_id}/${stage_count}`, this.#padding, cursor_y, '#8fd3ff');
    cursor_y += this.#line_height;

    if (stage) {
      this.#render_line(
        ctx,
        `Scene: ${stage.background_image_name}`,
        this.#padding,
        cursor_y,
        '#b9b9b9'
      );
      cursor_y += this.#line_height;
    }

    if (progress_summary) {
      const score_label = progress_summary.best_score === null ? '--' : progress_summary.best_score.toString();
      this.#render_line(ctx, `Best score: ${score_label}`, this.#padding, cursor_y, '#9bd1ff');
      cursor_y += this.#line_height;

      this.#render_line(
        ctx,
        `Medal: ${progress_summary.medal_label.toUpperCase()}`,
        this.#padding,
        cursor_y,
        '#9bd1ff'
      );
      cursor_y += this.#line_height;

      if (progress_summary.next_target !== null) {
        this.#render_line(
          ctx,
          `Next target: ${progress_summary.next_target}`,
          this.#padding,
          cursor_y,
          '#8fd3ff'
        );
        cursor_y += this.#line_height;
      }
    }

    if (last_input) this.#render_line(ctx, `Last input: ${last_input}`, this.#padding, cursor_y, '#8fd3ff');

    ctx.restore();
  }

  #render_line(
    ctx: CanvasRenderingContext2D,
    text: string,
    x: number,
    y: number,
    color: string
  ): void {
    ctx.fillStyle = color;
    ctx.fillText(text, x, y);
  }
}
