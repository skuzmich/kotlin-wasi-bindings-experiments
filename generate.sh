#!/usr/bin/env bash

cargo run --color=always --bin witx-bindgen --manifest-path \
    ../../proj/wasi-witx-kotlin-hacks/crates/witx-bindgen/Cargo.toml >\
    ./src/wasmMain/kotlin/WasiGenerated.kt

java -jar ~/bin/ktfmt-0.42-jar-with-dependencies.jar \
  ./src/wasmMain/kotlin/WasiGenerated.kt