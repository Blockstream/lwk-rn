import { NativeLoader } from './NativeLoader';
import { Transaction } from './Transaction';

export class Pset extends NativeLoader {
  id: string = '';

  async from(id: string): Promise<Pset> {
    this.id = id;
    return this;
  }

  async create(base64: string): Promise<Pset> {
    this.id = await this._lwk.createPset(base64);
    return this;
  }

  async extractTx(): Promise<Transaction> {
    let id = await this._lwk.psetExtractTx(this.id);
    return new Transaction().from(id);
  }

  async asString(): Promise<string> {
    return this._lwk.psetAsString(this.id);
  }
}
