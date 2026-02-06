export interface StageObject {
  x: number;
  y: number;
  image_name: string;
}

export interface Stage {
  id: number;
  background_image_name: string;
  stage_objects: StageObject[];
  bronze_medal_points: number;
  silver_medal_points: number;
  gold_medal_points: number;
}
