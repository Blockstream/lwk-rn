package io.lwkrn

import lwk.Network
import lwk.WalletTx
import lwk.Address
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
    "timestamp" to transaction.timestamp()?.toInt()
  )
}

fun getAddressObject(address: Address): MutableMap<String, Any?> {
  return mutableMapOf<String, Any?>(
    "description" to address.toString(),
    "is_blinded" to address.isBlinded(),
    "qr_code_text" to address.qrCodeText(),
    "script_pubkey" to address.scriptPubkey().toString()
  )
}
