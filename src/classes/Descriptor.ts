import { NativeLoader } from './NativeLoader';

export class Descriptor extends NativeLoader {
  id: string = '';

  async from(id: string): Promise<Descriptor> {
    this.id = id;
    return this;
  }

  async create(descriptor: string): Promise<Descriptor> {
    this.id = await this._lwk.WolletDescriptorInit(descriptor);
    return this;
  }

  async asString(): Promise<string> {
    return this._lwk.wolletDescriptorDescription(this.id);
  }
  async isMainnet(): Promise<boolean> {
    return this._lwk.wolletDescriptorIsMainnet(this.id);
  }
  async deriveBlindingKey(scriptPubkey: string): Promise<string> {
    return this._lwk.wolletDescriptorDeriveBlindingKey(this.id, scriptPubkey);
  }
  async scriptPubkey(extInt: string, index: number): Promise<string> {
    return this._lwk.wolletDescriptorScriptPubkey(this.id, extInt, index);
  }
}
