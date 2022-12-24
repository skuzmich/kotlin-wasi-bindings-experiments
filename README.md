# Kotlin/Wasm WASI Bindings Experiments

Uses (in-progress) unsafe linear memory APSs `kotlin.wasm.unsafe.*` and annotation `kotlin.WasmImport`.

Bindings are generated from https://github.com/skuzmich/wasi-witx-kotlin-hacks/blob/kotlin-hacks/crates/witx-bindgen/src/lib.rs for `wasi_snapshot_preview1` version of WASI API.

Needs a branch https://github.com/JetBrains/kotlin/tree/skuzmich/wit-prototyping installed to maven local:

```
~/kotlin $ ./gradlew install --parallel
```


# Testing


```
./gradlew wasmTest
```
