export class AssetLoader {
  #json_cache: Map<string, unknown> = new Map();
  #image_cache: Map<string, HTMLImageElement> = new Map();

  async load_json<result_type>(path: string): Promise<result_type> {
    const cached = this.#json_cache.get(path) as result_type | undefined;
    if (cached) return cached;

    const response = await fetch(path);
    if (!response.ok) throw new Error(`Failed to load JSON: ${path}`);

    const data = (await response.json()) as result_type;
    this.#json_cache.set(path, data);
    return data;
  }

  async load_image(path: string): Promise<HTMLImageElement> {
    const cached = this.#image_cache.get(path);
    if (cached) return cached;

    return await new Promise((resolve, reject) => {
      const image = new Image();
      this.#image_cache.set(path, image);
      image.onload = () => resolve(image);
      image.onerror = error => {
        this.#image_cache.delete(path);
        reject(new Error(`Failed to load image: ${path}`));
      };
      image.src = path;
    });
  }

  get_cached_image(path: string): HTMLImageElement | null {
    return this.#image_cache.get(path) ?? null;
  }
}
