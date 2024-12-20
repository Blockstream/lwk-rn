import { NativeLoader } from './NativeLoader';

export class Bip extends NativeLoader {
  id: string = '';

  async newBip49(): Promise<Bip> {
    this.id = await this._lwk.newBip49();
    return this;
  }
  async newBip84(): Promise<Bip> {
    this.id = await this._lwk.newBip84();
    return this;
  }
  async newBip87(): Promise<Bip> {
    this.id = await this._lwk.newBip87();
    return this;
  }
}
