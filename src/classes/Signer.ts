import { NativeLoader } from './NativeLoader';
import { Pset } from './Pset';
import { Network } from '../lib/enums';
import { Descriptor } from './Descriptor';
import { Bip } from './Bip';

export class Signer extends NativeLoader {
  id: string = '';

  async create(mnemonic: string, network: Network): Promise<Signer> {
    if (!Object.values(Network).includes(network)) {
      throw `Invalid network passed. Allowed values are ${Object.values(Network)}`;
    }
    this.id = await this._lwk.createSigner(mnemonic, network);
    return this;
  }

  async sign(pset: Pset): Promise<Pset> {
    let newPsetId = await this._lwk.sign(this.id, pset.id);
    return new Pset().from(newPsetId);
  }

  async wpkhSlip77Descriptor(): Promise<Descriptor> {
    let newId = await this._lwk.wpkhSlip77Descriptor(this.id);
    return new Descriptor().from(newId);
  }

  async createRandomSigner(network: Network): Promise<Signer> {
    if (!Object.values(Network).includes(network)) {
      throw `Invalid network passed. Allowed values are ${Object.values(Network)}`;
    }
    this.id = await this._lwk.createRandomSigner(network);
    return this;
  }

  async keyoriginXpub(bip: Bip): Promise<string> {
    return await this._lwk.keyoriginXpub(this.id, bip.id);
  }

  async mnemonic(): Promise<string> {
    return await this._lwk.mnemonic(this.id);
  }
}
