#!/bin/sh

# Manually fetch and edit lwk repository

REPO=https://github.com/Blockstream/lwk
TAG=bindings_0.9.0

rm -rf rust_modules
mkdir -p rust_modules
cd rust_modules

git clone $REPO
cd lwk
git checkout $TAG
cd ..

mv -f lwk/lwk_bindings .

if [[ "$OSTYPE" == "darwin"* ]]; then
   sed -i '' 's/name = "lwk_bindings"/name = "lwk"/g' lwk_bindings/Cargo.toml
else
   sed -i 's/name = "lwk_bindings"/name = "lwk"/g' lwk_bindings/Cargo.toml
fi

cat << EOF >> lwk_bindings/Cargo.toml
[profile.release-smaller]
inherits = "release"
opt-level = 'z'      # Optimize for size.
lto = true           # Enable Link Time Optimization
codegen-units = 1    # Reduce number of codegen units to increase optimizations.
panic = "abort"      # Abort on panic
strip = true         # Partially strip symbols from binary
EOF