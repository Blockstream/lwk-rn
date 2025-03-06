package io.lwkrn

import lwk.Network
import lwk.WalletTx
import lwk.Address
import lwk.Chain
import lwk.WalletTxOut
import java.util.*

fun setNetwork(networkStr: String? = "testnet"): Network {
  return when (networkStr) {
    "testnet" -> Network.testnet()
    "mainnet" -> Network.mainnet()
    "regtest" -> Network.regtestDefault()
    else -> Network.testnet()
  }
}

fun getNetworkString(network: Network): String {
  if (network.isMainnet()) {
    return "mainnet"
  } else {
    return "testnet"
  }
}

fun randomId() = UUID.randomUUID().toString()

fun getTransactionObject(transaction: WalletTx): MutableMap<String, Any?> {
  return mutableMapOf<String, Any?>(
    "fee" to transaction.fee().toInt(),
    "balance" to transaction.balance().mapValues { it.value.toInt() },
    "type" to transaction.type(),
    "txid" to transaction.txid().toString(),
    "height" to transaction.height()?.toInt(),
    "timestamp" to transaction.timestamp()?.toInt(),
    "inputs" to transaction.inputs().map { getWalletTxOutObject(it) },
    "outputs" to transaction.outputs().map { getWalletTxOutObject(it) }
  )
}

fun getAddressObject(address: Address): MutableMap<String, Any> {
  return mutableMapOf<String, Any>(
    "description" to address.toString(),
    "is_blinded" to address.isBlinded(),
    "qr_code_text" to address.qrCodeText(),
    "script_pubkey" to address.scriptPubkey().toString()
  )
}
fun getWalletTxOutObject(out: WalletTxOut?): MutableMap<String, Any?> {
  return mutableMapOf<String, Any?>(
        "ext_int" to if (out?.extInt() == Chain.EXTERNAL) "external" else "internal",
        "height" to out?.height()?.toInt(),
        "outpoint" to mutableMapOf<String, Any?> (
            "txid" to out?.outpoint()?.txid().toString(),
            "vout" to out?.outpoint()?.vout()?.toInt()
        ),
        "script_pubkey" to out?.scriptPubkey().toString(),
        "unblinded" to mutableMapOf<String, Any?> (
            "asset" to out?.unblinded()?.asset(),
            "assetBf" to out?.unblinded()?.assetBf(),
            "value" to out?.unblinded()?.value()?.toInt(),
            "valueBf" to out?.unblinded()?.valueBf()
        ),
        "wildcard_index" to out?.wildcardIndex()?.toInt()
  )
}

fun getChain(chain: String? = "external"): Chain {
  return when (networkStr) {
    "external": Chain.external
    "internal": Chain.internal
    else: Chain.external
  }
}
fun ByteArray.toHexString() = joinToString("") {
    Integer.toUnsignedString(java.lang.Byte.toUnsignedInt(it), 16).padStart(2, '0')
}
