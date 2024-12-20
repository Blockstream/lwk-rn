import type { Address, Balance, WolletTx } from '../lib/types';
import { Network } from '../lib/enums';
import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-lwk-module' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

export interface NativeLwk {
  // Descriptor
  createDescriptor(descriptor: string): string;
  descriptorAsString(id: string): string;

  // Electrum Client
  initElectrumClient(
    electrumUrl: string,
    tls: boolean,
    validateDomain: boolean
  ): string;
  defaultElectrumClient(network: Network): string;
  broadcast(clientId: string, txId: string): string;

  // Signer
  createSigner(mnemonic: string, network: Network): string;
  sign(signerId: string, psetId: string): string;
  wpkhSlip77Descriptor(signerId: string): string;
  keyoriginXpub(signerId: string, bipId: string): string;
  createRandomSigner(network: Network): string;
  mnemonic(signerId: string): string;

  // Wollet
  createWollet(
    network: Network,
    descriptorId: string,
    datadir: string | null
  ): string;
  fullScan(wolletId: string, clientId: string): string;
  applyUpdate(wolletId: string, updateId: string): string;
  getTransactions(wolletId: string): Array<WolletTx>;
  getDescriptor(wolletId: string): string;
  getBalance(wolletId: string): Balance;
  getAddress(wolletId: string, index: number | null): Address;
  finalize(wolletId: string, psetId: string): string;
  waitTx(wolletId: string, txid: string, clientId: string): WolletTx;

  // Transaction
  createTransaction(hex: string): string;
  txId(txId: string): string;
  txFee(txId: string, policyAsset: string): number;
  txAsString(txId: string): string;

  // Pset
  psetAsString(psetId: string): string;
  psetExtractTx(psetId: string): string;
  psetIssuanceAsset(id: string, index: number): string;
  psetissuanceToken(id: string, index: number): string;

  // TxBuilder
  createTxBuilder(network: string): string;
  txBuilderAddBurn(id: string, satoshi: number, asset: string): null;
  txBuilderAddLbtcRecipient(id: string, address: string, satoshi: number): null;
  txBuilderAddRecipient(
    id: string,
    address: string,
    satoshi: number,
    asset: string
  ): null;
  txBuilderDrainLbtcTo(id: string, address: string): null;
  txBuilderDrainLbtcWallet(id: string): null;
  txBuilderFeeRate(id: string, rate: number | null): null;
  txBuilderFinish(id: string, wolletId: string): string;
  txBuilderIssueAsset(
    id: string,
    assetSats: number,
    assetReceiver: string | null,
    tokenSats: number,
    tokenReceiver: string | null,
    contract: string | null
  ): null;
  txBuilderReissueAsset(
    id: string,
    assetToReissue: string,
    satoshiToReissue: number,
    assetReceiver: string | null,
    issuanceTx: string | null
  ): null;

  // Contract
  createContract(
    domain: string,
    issuerPubkey: string,
    name: string,
    precision: number,
    ticker: string,
    version: number
  ): string;
  contractAsString(id: string): string;

  // Bip
  newBip49(): string;
  newBip84(): string;
  newBip87(): string;
}

export class NativeLoader {
  protected _lwk: NativeLwk;

  constructor() {
    this._lwk = NativeModules.LwkRnModule
      ? NativeModules.LwkRnModule
      : new Proxy(
          {},
          {
            get() {
              throw new Error(LINKING_ERROR);
            },
          }
        );
  }
}
