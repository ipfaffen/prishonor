export class AssetLoader {
  #json_cache: Map<string, unknown> = new Map();

  async load_json<result_type>(path: string): Promise<result_type> {
    const cached = this.#json_cache.get(path) as result_type | undefined;
    if (cached) return cached;

    const response = await fetch(path);
    if (!response.ok) throw new Error(`Failed to load JSON: ${path}`);

    const data = (await response.json()) as result_type;
    this.#json_cache.set(path, data);
    return data;
  }
}
