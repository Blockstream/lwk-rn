# Liquid Wallet Kit - react native

**LWK-rn** is a React Native module for [Liquid Wallet Kit](https://github.com/Blockstream/lwk). Its goal is to provide all the necessary building blocks for mobile development of a liquid wallet.

**NOTE: LWK and LWK-rn is in public beta and still undergoing significant development. Use it at your own risk.**

_Please consider reviewing, experimenting and contributing_

_Thanks for taking a look!_

## Installation

Using npm:

```sh
npm install lwk-rn
```

Using yarn:

```sh
yarn add lwk-rn
```

Note: Use android sdk version >= 24 and iOS >= v13 .

## Usage

Import LWK-rn library

```js
import { Mnemonic, Network, Signer, Wollet } from 'lwk-rn';
```

Create a signer for a mnemonic and a network
```js
let mnemonic = new Mnemonic("abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon about"); 
let network = Network.testnet();
let signer = new Signer(mnemonic, network);
console.log(mnemonic.toString());
```

Create and update a wollet from the signer descriptor
```js
let singlesigDesc = signer.wpkhSlip77Descriptor();
let wollet = new Wollet(network, singlesigDesc, undefined);
let client = network.defaultElectrumClient();
let update = client.fullScan(wollet);
if (update) { 
  wollet.applyUpdate(update);
}
```

Get a new unused address
```js
let latest_address = wollet.address(undefined); 
console.log(latest_address.address().scriptPubkey().toString());
```

Get balance as `[AssetId : UInt64]`
```js
let balance = wollet.balance();
console.log(balance);
for (var b of balance.entries()) {
  console.log("asset: ", b[0], ", value: ", b[1]);
}
```

Get a transaction list
```js
let txs = wollet.transactions();
console.log(txs);
for (var tx of txs) {
  for (var output of tx.outputs()) {
    let script_pubkey = output?.scriptPubkey().toString();
    let value = output?.unblinded().value().toString();
    console.log("script_pubkey: ", script_pubkey, ", value: ", value);
  }
}
```

## Build

LWK-rn repository contains the pre-generated lwk bindings for android and ios. 

Follow the steps to generate bindings by your own:

Install C++ tooling

```sh
# For MacOS, using homebrew:
brew install cmake ninja clang-format
# For Debian flavoured Linux:
apt-get install cmake ninja clang-format
```

Add the Android specific targets
```sh
rustup target add \
    aarch64-linux-android \
    armv7-linux-androideabi \
    i686-linux-android \
    x86_64-linux-android
# Install cargo-ndk
cargo install cargo-ndk
```

Add the iOS specific targets
```sh
rustup target add \
    aarch64-apple-ios \
    aarch64-apple-ios-sim \
    x86_64-apple-ios
# Ensure xcodebuild is available
xcode-select --install
```

Install deps and `uniffi-bindgen-react-native`.

```sh
$ yarn install
```

Fetch LWK library with some hacks.
> The script changes path to avoid using workspace configuration and rust version. The project require rust >= v1.18 . The scipt replacing package name in `Cargo.toml` for a library name bug in `uniffi-bindgen-react-native`.
```sh
$ sh fetch_lwk.sh
```

Generate bindings for android and ios:
```sh
$ yarn ubrn:android
$ yarn ubrn:ios
```

## Example

Open demo application in `./example/` folder and read the code in `./example/src/App.tsx` .

You could run the example on the Android demo app on device/emulator by:
```sh
$ yarn example android
```

Or you could run the iOS demo app by:
```sh
$ cd example
$ bundle install
$ bundle exec pod install # every time you update your native dependencies
$ cd ..
$ yarn example ios
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## HOWTO

See the [how-to](HOW-TO.md) to learn how works lwk-rn react native module and the repo code.

## Greetings

Thanks to all Blockstream LWK team for the amazing library.

A special thanks to @rcasatta to help me about packaging library for iOS and Android.

A big thanks to @BlakeKaufman because give me the opportunity to build this library with a [Bounty](https://github.com/BlakeKaufman/BlitzWallet/issues/4) and made contributions by itself.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
using [uniffi-bindgen-react-native](https://github.com/jhugman/uniffi-bindgen-react-native)
