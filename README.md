# Kotlin/Wasm WASI Bindings Experiments

Uses new unsafe linear memory APSs `kotlin.wasm.unsafe.*` and annotation `kotlin.WasmImport`.

Bindings are generated from https://github.com/skuzmich/wasi-witx-kotlin-hacks/blob/kotlin-hacks/crates/witx-bindgen/src/lib.rs for `wasi_snapshot_preview1` version of WASI API.

Needs a fresh [kotlin](https://github.com/JetBrains/kotlin) installed to maven local:

```
~/kotlin $ ./gradlew install --parallel
```


# Testing


```
./gradlew compileTestProductionExecutableKotlinWasm
# Node version v20.0.0-v8-canary2022112061c569ba0d
node --experimental-wasi-unstable-preview1 --experimental-wasm-gc runner.mjs
```
