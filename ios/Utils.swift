
import Foundation
import LiquidWalletKit

func setNetwork(networkStr: String?) -> Network {
    switch (networkStr) {
    case "testnet": return Network.testnet()
    case "mainnet": return Network.mainnet()
    case "regtest": return Network.regtestDefault()
    default: return Network.testnet()
    }
}

func getNetworkString(network: Network) -> String {
    if network.isMainnet() { 
        return "mainnet"
    } else { 
        return "testnet"
    }
}

func randomId() -> String {
    return UUID().uuidString
}

func getTransactionObject(transaction: WalletTx?) -> [String: Any?] {
    return [
        "fee": transaction?.fee(),
        "balance": transaction?.balance(),
        "type": transaction?.type(),
        "txid": transaction?.txid().description,
        "tx": transaction?.tx().bytes(),
        "height": transaction?.height(),
        "timestamp": transaction?.timestamp(),
        "inputs": transaction?.inputs().compactMap { getWalletTxOutObject(out: $0) },
        "outputs": transaction?.outputs().compactMap { getWalletTxOutObject(out: $0) }
    ]
}

func getWalletTxOutObject(out: WalletTxOut?) -> [String: Any?] {
    return [
        "ext_int": out?.extInt() == .external ? "external" : "internal",
        "height": out?.height(),
        "outpoint": [
            "txid": out?.outpoint().txid().description as? Any,
            "vout": out?.outpoint().vout() as? Any
        ],
        "script_pubkey": out?.scriptPubkey().description,
        "unblinded": [
            "asset": out?.unblinded().asset() as? Any,
            "assetBf": out?.unblinded().assetBf() as? Any,
            "value": out?.unblinded().value() as? Any,
            "valueBf": out?.unblinded().valueBf() as? Any
        ],
        "wildcard_index": out?.wildcardIndex()
        ]
}

func getAddressObject(address: Address?) -> [String: Any?] {
    return [
        "description": address?.description as Any,
        "is_blinded": address?.isBlinded() as Any,
        "qr_code_text": try? address?.qrCodeText() as? Any,
        "script_pubkey": address?.scriptPubkey().description as Any,
    ] as [String: Any?]
}

func getChain(chain: String? = "external") -> Chain {
    switch (chain) {
    case "external": return Chain.external
    case "internal": return Chain.internal
    default: return Chain.external
    }
}

extension Data {
    var hex: String {
        return reduce("") {$0 + String(format: "%02x", $1)}
    }
}