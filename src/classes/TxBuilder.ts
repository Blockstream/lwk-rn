import { NativeLoader } from './NativeLoader';
import { Wollet } from './Wollet';
import { Pset } from './Pset';
import { Transaction } from './Transaction';
import type { Contract } from '../lib/types';

export class TxBuilder extends NativeLoader {
  id: string = '';

  async from(id: string): Promise<TxBuilder> {
    this.id = id;
    return this;
  }

  async create(network: string): Promise<TxBuilder> {
    this.id = await this._lwk.createTxBuilder(network);
    return this;
  }

  async addBurn(satoshi: number, asset: string): Promise<null> {
    return await this._lwk.txBuilderAddBurn(this.id, satoshi, asset);
  }
  async addLbtcRecipient(address: string, satoshi: number): Promise<null> {
    return await this._lwk.txBuilderAddLbtcRecipient(this.id, address, satoshi);
  }
  async addRecipient(
    address: string,
    satoshi: number,
    asset: string
  ): Promise<null> {
    return await this._lwk.txBuilderAddRecipient(
      this.id,
      address,
      satoshi,
      asset
    );
  }
  async drainLbtcTo(address: string): Promise<null> {
    return await this._lwk.txBuilderDrainLbtcTo(this.id, address);
  }
  async drainLbtcWallet(): Promise<null> {
    return await this._lwk.txBuilderDrainLbtcWallet(this.id);
  }
  async feeRate(rate: number | null): Promise<null> {
    return await this._lwk.txBuilderFeeRate(this.id, rate);
  }

  async finish(wollet: Wollet): Promise<Pset> {
    let psetId = await this._lwk.txBuilderFinish(this.id, wollet.id);
    return new Pset().from(psetId);
  }

  async issueAsset(
    assetSats: number,
    assetReceiver: string | null,
    tokenSats: number,
    tokenReceiver: string | null,
    contract: Contract | null
  ): Promise<null> {
    let contractId =
      contract == null
        ? null
        : this._lwk.createContract(
            contract.domain,
            contract.issuerPubkey,
            contract.name,
            contract.precision,
            contract.ticker,
            contract.version
          );
    return await this._lwk.txBuilderIssueAsset(
      this.id,
      assetSats,
      assetReceiver,
      tokenSats,
      tokenReceiver,
      contractId
    );
  }

  async reissueAsset(
    assetToReissue: string,
    satoshiToReissue: number,
    assetReceiver: string | null,
    issuanceTx: Transaction | null
  ): Promise<null> {
    let transactionHex =
      issuanceTx == null ? null : await issuanceTx?.asString();
    return await this._lwk.txBuilderReissueAsset(
      this.id,
      assetToReissue,
      satoshiToReissue,
      assetReceiver,
      transactionHex
    );
  }
}
