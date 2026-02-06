import { copyFile as copy_file, mkdir as make_dir, readdir as read_dir, readFile as read_file, stat as stat_file, writeFile as write_file } from 'node:fs/promises';
import { dirname as dir_name, join as join_path } from 'node:path';

const source_images_path = 'src/main/java/resources/images';
const source_stage_path = 'src/main/java/resources/images/stage';
const source_stages_json_path = 'src/main/java/resources/json/stages.json';
const target_images_path = 'public/images';
const target_stage_path = 'public/images/stage';
const target_stages_json_path = 'public/data/stages.json';

const main = async () => {
  await copy_directory(source_images_path, target_images_path);
  await copy_directory(source_stage_path, target_stage_path);
  await write_stage_json();
};

const copy_directory = async (source_path, target_path) => {
  if (!source_path || !target_path) return;

  await make_dir(target_path, {recursive:true});
  const entries = await read_dir(source_path);

  for (const entry of entries) {
    const source_entry_path = join_path(source_path, entry);
    const target_entry_path = join_path(target_path, entry);
    const entry_stat = await stat_file(source_entry_path);

    if (entry_stat.isDirectory()) {
      await copy_directory(source_entry_path, target_entry_path);
      continue;
    }

    await copy_file(source_entry_path, target_entry_path);
  }
};

const write_stage_json = async () => {
  const raw_data = await read_file(source_stages_json_path, 'utf-8');
  const stages = JSON.parse(raw_data);
  if (!Array.isArray(stages)) return;

  const normalized = stages
    .map(stage_item => normalize_stage(stage_item))
    .filter(stage_item => stage_item !== null);

  const payload = JSON.stringify(normalized, null, 2);
  await make_dir(dir_name(target_stages_json_path), {recursive:true});
  await write_file(target_stages_json_path, payload, 'utf-8');
};

const normalize_stage = stage_item => {
  if (!stage_item || typeof stage_item !== 'object') return null;
  if (!Number.isFinite(stage_item.id)) return null;

  const background = stage_item.background ?? {};
  const stage_objects = Array.isArray(stage_item.stageObjects)
    ? stage_item.stageObjects
        .filter(stage_object => Number.isFinite(stage_object.positionX) && Number.isFinite(stage_object.positionY))
        .map(stage_object => ({
          x: stage_object.positionX,
          y: stage_object.positionY,
          image_name: stage_object.background?.imageName ?? ''
        }))
    : [];

  return {
    id: stage_item.id,
    background_image_name: background.imageName ?? '',
    stage_objects,
    bronze_medal_points: stage_item.bronzeMedalPoints ?? 0,
    silver_medal_points: stage_item.silverMedalPoints ?? 0,
    gold_medal_points: stage_item.goldMedalPoints ?? 0
  };
};

main().catch(error => {
  console.error(error);
  process.exitCode = 1;
});
