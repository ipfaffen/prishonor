export interface Position {
  x: number;
  y: number;
}

export interface Size {
  width: number;
  height: number;
}

export interface Bounds extends Position, Size {}

export interface Color {
  r: number;
  g: number;
  b: number;
  a?: number;
}

export enum Direction {
  UP = 'UP',
  DOWN = 'DOWN',
  LEFT = 'LEFT',
  RIGHT = 'RIGHT'
}

export enum SkillType {
  MAGIC_PROJECTILE = 'MAGIC_PROJECTILE',
  PORTAL = 'PORTAL'
}

export enum ModeEnum {
  STAGE = 'STAGE',
  MENU = 'MENU'
}

export interface Renderable {
  render(ctx: CanvasRenderingContext2D): void;
  update(delta_time: number): void;
}
