import { Text, View, StyleSheet } from 'react-native';
import { Mnemonic, Network, Signer, Wollet } from 'lwk-rn';

let mnemonic = new Mnemonic("abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon about"); 
let network = Network.testnet();
let signer = new Signer(mnemonic, network);
console.log(mnemonic.toString());

let singlesigDesc = signer.wpkhSlip77Descriptor();
let wollet = new Wollet(network, singlesigDesc, undefined);

// update wallet
let client = network.defaultElectrumClient();
let update = client.fullScan(wollet);
if (update) { 
  wollet.applyUpdate(update);
}

// generate last unused address
let latest_address = wollet.address(undefined); 
console.log(latest_address.address().scriptPubkey().toString());

// show balance
let balance = wollet.balance();
console.log(balance);
for (var b of balance.entries()) {
  console.log("asset: ", b[0], ", value: ", b[1]);
}

// show transactions
let txs = wollet.transactions();
console.log(txs);
for (var tx of txs) {
  for (var output of tx.outputs()) {
    let script_pubkey = output?.scriptPubkey().toString();
    let value = output?.unblinded().value().toString();
    console.log("script_pubkey: ", script_pubkey, ", value: ", value);
  }
}

const result = balance.get(network.policyAsset())?.toString();

export default function App() {
  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
