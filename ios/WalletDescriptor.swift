import LiquidWalletKit 

extension LwkRnModule {

    /* WolletDescriptor */
    @objc
    func wolletDescriptorInit(_
                          descriptor: String,
                          resolve: RCTPromiseResolveBlock,
                          reject: RCTPromiseRejectBlock
    ) -> Void {
        do {
            let id = randomId()
            _descriptors[id] = try WolletDescriptor(descriptor: descriptor)
            resolve(id)
        } catch {
            reject("WolletDescriptor UBUT error", error.localizedDescription, error)
        }
    }
    @objc
    func wolletDescriptorDescription(_
                            keyId: String,
                            resolve: @escaping RCTPromiseResolveBlock,
                            reject: @escaping RCTPromiseRejectBlock
    ) {
        resolve(_descriptors[keyId]!.description)
    }
    @objc
    func wolletDescriptorIsMainnet(_
                            keyId: String,
                            resolve: @escaping RCTPromiseResolveBlock,
                            reject: @escaping RCTPromiseRejectBlock
    ) {
        resolve(_descriptors[keyId]!.isMainnet())
    }
    @objc
    func wolletDescriptorDeriveBlindingKey(_
                            keyId: String,
                            scriptPubkey: String,
                            resolve: @escaping RCTPromiseResolveBlock,
                            reject: @escaping RCTPromiseRejectBlock
    ) {
      let scriptPubkey = try? Script(hex: scriptPubkey)
      let descriptor = _descriptors[keyId]
      guard let scriptPubkey = scriptPubkey,
            let secretKey = descriptor!.deriveBlindingKey(scriptPubkey: scriptPubkey) else {
        reject("WolletDescriptor deriveBlindingKey error", "no derived secret key", CancellationError())
        return
      }
      resolve(secretKey.bytes().hex)
    }

    @objc
    func wolletDescriptorScriptPubkey(_
                            keyId: String,
                            extInt: String,
                            index: NSNumber,
                            resolve: @escaping RCTPromiseResolveBlock,
                            reject: @escaping RCTPromiseRejectBlock
    ) {
      do {
        let extInt = getChain(chain: keyId)
        let descriptor = _descriptors[keyId]
        let script = try descriptor!.scriptPubkey(extInt: extInt, index: index.uint32Value)
        resolve(script.bytes().hex)
      } catch {
        reject("WolletDescriptor scriptPubkey error", error.localizedDescription, error)
      }
    }
}