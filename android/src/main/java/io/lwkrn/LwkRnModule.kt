package io.lwkrn

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Dynamic
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import lwk.Address
import lwk.Contract
import lwk.ElectrumClient
import lwk.Mnemonic
import lwk.Pset
import lwk.Signer
import lwk.Transaction
import lwk.TxBuilder
import lwk.Txid
import lwk.Update
import lwk.WalletTx
import lwk.Wollet
import lwk.WolletDescriptor

class LwkRnModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {
  override fun getName() = "LwkRnModule"
  override fun getConstants(): MutableMap<String, Any> {
    return hashMapOf("count" to 1)
  }

  private var _descriptors = mutableMapOf<String, WolletDescriptor>()
  private var _electrumClients = mutableMapOf<String, ElectrumClient>()
  private var _wollets = mutableMapOf<String, Wollet>()
  private var _updates = mutableMapOf<String, Update>()
  private var _walletTxs = mutableMapOf<String, WalletTx>()
  private var _transactions = mutableMapOf<String, Transaction>()
  private var _psets = mutableMapOf<String, Pset>()
  private var _signers = mutableMapOf<String, Signer>()
  private var _txBuilders = mutableMapOf<String, TxBuilder>()
  private var _contracts = mutableMapOf<String, Contract>()
  private var _bips = mutableMapOf<String, Bip>()

  /* Descriptor */

  @ReactMethod
  fun createDescriptor(
    descriptor: String, result: Promise
  ) {
    try {
      val id = randomId()
      _descriptors[id] = WolletDescriptor(descriptor)
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("WolletDescriptor create error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun descriptorAsString(
    keyId: String,
    result: Promise
  ) {
    result.resolve(_descriptors[keyId]!!.toString())
  }

  /* Bip */

  @ReactMethod
  fun newBip49(
    result: Promise
  ) {
    try {
      val id = randomId()
      _bips[id] = Bip.newBip49()
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("Bip newBip49 error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun newBip84(
    result: Promise
  ) {
    try {
      val id = randomId()
      _bips[id] = Bip.newBip84()
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("Bip newBip84 error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun newBip87(
    result: Promise
  ) {
    try {
      val id = randomId()
      _bips[id] = Bip.newBip87()
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("Bip newBip87 error", error.localizedMessage, error)
    }
  }

  /* Signer */

  @ReactMethod
  fun createSigner(
    mnemonic: String,
    network: String,
    result: Promise
  ) {
    try {
      val id = randomId()
      val mnemonicObj = Mnemonic(mnemonic)
      val networkObj = setNetwork(network)
      _signers[id] = Signer(mnemonicObj, networkObj)
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("Signer create error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun sign(
    signerId: String,
    psetId: String,
    result: Promise
  ) {
    try {
      val id = randomId()
      val signer = _signers[signerId]
      val pset = _psets[psetId]
      _psets[id] = signer!!.sign(pset!!)
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("Signer sign error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun wpkhSlip77Descriptor(
    signerId: String,
    result: Promise
  ) {
    try {
      val id = randomId()
      val signer = _signers[signerId]
      _descriptors[id] = signer!!.wpkhSlip77Descriptor()
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("Signer wpkhSlip77Descriptor error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun keyoriginXpub(
    signerId: String,
    bipId: String,
    result: Promise
  ) {
    try {
      val id = randomId()
      val signer = _signers[signerId]
      val bip = _bips[bipId]
      val res = signer!!.keyoriginXpub(bip!!)
      result.resolve(res)
    } catch (error: Throwable) {
      result.reject("Signer keyoriginXpub error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun mnemonic(
    signerId: String,
    result: Promise
  ) {
    try {
      val id = randomId()
      val signer = _signers[signerId]
      val res = signer!!.mnemonic()
      result.resolve(res)
    } catch (error: Throwable) {
      result.reject("Signer mnemonic error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun createRandomSigner(
    network: String,
    result: Promise
  ) {
    try {
      val id = randomId()
      val networkObj = setNetwork(network)
      _signers[id] = Signer.random(networkObj)
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("Signer createRandomSigner error", error.localizedMessage, error)
    }
  }

  /* Electrum client */

  @ReactMethod
  fun initElectrumClient(
    electrumUrl: String,
    tls: Boolean,
    validateDomain: Boolean,
    result: Promise
  ) {
    try {
      val id = randomId()
      _electrumClients[id] = ElectrumClient(electrumUrl, tls, validateDomain)
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("ElectrumClient create error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun defaultElectrumClient(
    network: String,
    result: Promise
  ) {
    try {
      val id = randomId()
      val networkObj = setNetwork(network)
      _electrumClients[id] = networkObj.defaultElectrumClient()
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject(
        "ElectrumClient defaultElectrumClient error",
        error.localizedMessage,
        error
      )
    }
  }

  @ReactMethod
  fun broadcast(
    clientId: String,
    txId: String,
    result: Promise
  ) {
    Thread {
      try {
        val client = _electrumClients[clientId]
        val transaction = _transactions[txId]
        val txid = client!!.broadcast(transaction!!)
        result.resolve(txid.toString())
      } catch (error: Throwable) {
        result.reject(
          "ElectrumClient broadcast error",
          error.localizedMessage,
          error
        )
      }
    }.start()
  }

  @ReactMethod
  fun fullScan(
    wolletId: String,
    clientId: String,
    result: Promise
  ) {
    Thread {
      try {
        val id = randomId()
        val client = _electrumClients[clientId]
        val wollet = _wollets[wolletId]
        _updates[id] = client!!.fullScan(wollet!!)!!
        result.resolve(id)
      } catch (error: Throwable) {
        result.reject("ElectrumClient fullScan error", error.localizedMessage, error)
      }
    }.start()
  }

  /* Wollet */

  @ReactMethod
  fun createWollet(
    network: String,
    descriptorId: String,
    datadir: String?,
    result: Promise
  ) {
    try {
      val id = randomId()
      val networkObj = setNetwork(network)
      val descriptor = _descriptors[descriptorId]
      _wollets[id] = Wollet(networkObj, descriptor!!, datadir)
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("Wollet create error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun applyUpdate(
    wolletId: String,
    updateId: String,
    result: Promise
  ) {
    Thread {
      try {
        val wollet = _wollets[wolletId]
        val update = _updates[updateId]
        wollet!!.applyUpdate(update!!)
        result.resolve(null)
      } catch (error: Throwable) {
        result.reject("Wollet applyUpdate error", error.localizedMessage, error)
      }
    }.start()
  }

  @ReactMethod
  fun getTransactions(wolletId: String, result: Promise) {
    Thread {
      try {
        val wollet = _wollets[wolletId]
        val list = wollet!!.transactions()
        val transactions: MutableList<Map<String, Any?>> = mutableListOf()
        for (item in list) {
          var txObject = getTransactionObject(item)
          val randomId = randomId()
          _walletTxs[randomId] = item
          txObject["transaction"] = randomId
          transactions.add(txObject)
        }
        result.resolve(Arguments.makeNativeArray(transactions))
      } catch (error: Throwable) {
        result.reject("Wollet getTransactions error", error.localizedMessage, error)
      }
    }.start()
  }

  @ReactMethod
  fun getDescriptor(wolletId: String, result: Promise) {
    try {
      val wollet = _wollets[wolletId]
      result.resolve(wollet!!.descriptor().toString())
    } catch (error: Throwable) {
      result.reject("Wollet getDescriptor error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun getAddress(wolletId: String, index: Dynamic, result: Promise) {
    Thread {
      try {
        val wollet = _wollets[wolletId]
        val address = wollet!!.address(if (index.isNull) null else index.asInt().toUInt()).address()
        result.resolve(Arguments.makeNativeMap(getAddressObject(address)))
      } catch (error: Throwable) {
        result.reject("Wollet getAddress error", error.localizedMessage, error)
      }
    }.start()
  }

  @ReactMethod
  fun getBalance(wolletId: String, result: Promise) {
    Thread {
      try {
        val wollet = _wollets[wolletId]
        val balance = wollet!!.balance().mapValues { it.value.toInt() }
        result.resolve(Arguments.makeNativeMap(balance))
      } catch (error: Throwable) {
        result.reject("Wollet getBalance error", error.localizedMessage, error)
      }
    }.start()
  }

  @ReactMethod
  fun finalize(wolletId: String, psetId: String, result: Promise) {
    try {
      val wollet = _wollets[wolletId]
      val pset = _psets[psetId]
      val id = randomId()
      _psets[id] = wollet!!.finalize(pset!!)
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("Wollet finalize error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun waitTx(wolletId: String, txid: String, clientId: String, result: Promise) {
    Thread {
      try {
        val wollet = _wollets[wolletId]
        val client = _electrumClients[clientId]
        val walletTx = wollet!!.waitForTx(Txid(txid), client!!)
        val id = randomId()
        _walletTxs[id] = walletTx
        val txObject = getTransactionObject(walletTx)
        txObject["transaction"] = id
        result.resolve(Arguments.makeNativeMap(txObject))
      } catch (error: Throwable) {
        result.reject("Wollet waitTx error", error.localizedMessage, error)
      }
    }.start()
  }
  /* Transaction */

  @ReactMethod
  fun createTransaction(
    hex: String, result: Promise
  ) {
    try {
      val id = randomId()
      _transactions[id] = Transaction(hex)
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("Transaction create error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txId(txId: String, result: Promise) {
    try {
      val tx = _transactions[txId]
      result.resolve(tx!!.txid().toString())
    } catch (error: Throwable) {
      result.reject("Transaction toString error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txFee(txId: String, policyAsset: String, result: Promise) {
    try {
      val tx = _transactions[txId]
      result.resolve(tx!!.fee(policyAsset).toString())
    } catch (error: Throwable) {
      result.reject("Transaction toString error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txAsString(txId: String, result: Promise) {
    try {
      val tx = _transactions[txId]
      result.resolve(tx.toString())
    } catch (error: Throwable) {
      result.reject("Transaction toString error", error.localizedMessage, error)
    }
  }

  /* Pset */

  @ReactMethod
  fun psetAsString(psetId: String, result: Promise) {
    try {
      val pset = _psets[psetId]
      result.resolve(pset.toString())
    } catch (error: Throwable) {
      result.reject("Pset toString error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun psetExtractTx(psetId: String, result: Promise) {
    try {
      val id = randomId()
      val pset = _psets[psetId]
      _transactions[id] = pset!!.extractTx()
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("Pset extractTx error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun psetIssuanceAsset(id: String, index: Int, result: Promise) {
    try {
      val assetId = _psets[id]?.issuanceAsset(index.toUInt())
      result.resolve(assetId)
    } catch (error: Throwable) {
      result.reject("Pset issuanceAsset error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun psetIssuanceToken(id: String, index: Int, result: Promise) {
    try {
      val assetId = _psets[id]?.issuanceToken(index.toUInt())
      result.resolve(assetId)
    } catch (error: Throwable) {
      result.reject("Pset issuanceToken error", error.localizedMessage, error)
    }
  }

  /* TxBuilder */
  @ReactMethod
  fun createTxBuilder(
    network: String, result: Promise
  ) {
    try {
      val id = randomId()
      val networkObj = setNetwork(network)
      _txBuilders[id] = networkObj.txBuilder()
      result.resolve(id)
    } catch (error: Throwable) {
      result.reject("TxBuilder create error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txBuilderAddBurn(id: String, satoshi: Int, asset: String, result: Promise) {
    try {
      _txBuilders[id]!!.addBurn(satoshi.toULong(), asset)
      result.resolve(null)
    } catch (error: Throwable) {
      result.reject("TxBuilder addBurn error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txBuilderAddLbtcRecipient(id: String, address: String, satoshi: Int, result: Promise) {
    try {
      _txBuilders[id]!!.addLbtcRecipient(Address(address), satoshi.toULong())
      result.resolve(null)
    } catch (error: Throwable) {
      result.reject("TxBuilder addLbtcRecipient error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txBuilderAddRecipient(
    id: String,
    address: String,
    satoshi: Int,
    asset: String,
    result: Promise
  ) {
    try {
      _txBuilders[id]!!.addRecipient(Address(address), satoshi.toULong(), asset)
      result.resolve(null)
    } catch (error: Throwable) {
      result.reject("TxBuilder addRecipient error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txBuilderDrainLbtcTo(id: String, address: String, result: Promise) {
    try {
      _txBuilders[id]!!.drainLbtcTo(Address(address))
      result.resolve(null)
    } catch (error: Throwable) {
      result.reject("TxBuilder drainLbtcTo error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txBuilderDrainLbtcWallet(id: String, result: Promise) {
    try {
      _txBuilders[id]!!.drainLbtcWallet()
      result.resolve(null)
    } catch (error: Throwable) {
      result.reject("TxBuilder drainLbtcWallet error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txBuilderFeeRate(id: String, rate: Float, result: Promise) {
    try {
      _txBuilders[id]!!.feeRate(rate)
      result.resolve(null)
    } catch (error: Throwable) {
      result.reject("TxBuilder feeRate error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txBuilderFinish(id: String, wolletId: String, result: Promise) {
    try {
      val newId = randomId()
      val wollet = _wollets[wolletId]
      _psets[newId] = _txBuilders[id]!!.finish(wollet!!)
      result.resolve(newId)
    } catch (error: Throwable) {
      result.reject("TxBuilder finish error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txBuilderIssueAsset(
    id: String,
    assetSats: Int,
    assetReceiver: String?,
    tokenSats: Int,
    tokenReceiver: String?,
    contractId: String?,
    result: Promise) {
    try {
      val assetReceiver = assetReceiver?.let { Address(assetReceiver) }
      val tokenReceiver = tokenReceiver?.let { Address(tokenReceiver) }
      val contract = contractId?.let { _contracts[contractId] }
      _txBuilders[id]!!.issueAsset(assetSats.toULong(), assetReceiver, tokenSats.toULong(), tokenReceiver, contract)
      result.resolve(null)
    } catch (error: Throwable) {
      result.reject("TxBuilder issueAsset error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun txBuilderReissueAsset(
    id: String,
    assetToReissue: String,
    satoshiToReissue: Int,
    assetReceiver: String?,
    issuanceTx: String?,
    result: Promise) {
    try {
      val assetReceiver = assetReceiver?.let { Address(assetReceiver) }
      val issuanceTx = issuanceTx?.let { Transaction(issuanceTx) }
      _txBuilders[id]!!.reissueAsset(assetToReissue, satoshiToReissue.toULong(), assetReceiver, issuanceTx)
      result.resolve(null)
    } catch (error: Throwable) {
      result.reject("TxBuilder reissueAsset error", error.localizedMessage, error)
    }
  }
  /* Contract */
  @ReactMethod
  fun createContract(
    domain: String,
    issuerPubkey: String,
    name: String,
    precision: UByte,
    ticker: String,
    version: UByte,
    result: Promise) {
    try {
      val id = randomId()
      _contracts[id] = Contract(domain, issuerPubkey, name, precision, ticker, version)
      result.resolve(null)
    } catch (error: Throwable) {
      result.reject("Contract create error", error.localizedMessage, error)
    }
  }

  @ReactMethod
  fun contractAsString(
    id: String,
    result: Promise
  ) {
    result.resolve(_contracts[id]!!.toString())
  }

}
