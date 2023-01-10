import { instantiate } from "./build/js/packages/kotlin-wasi-bindings-wasm-test/kotlin/kotlin-wasi-bindings-wasm-test.uninstantiated.mjs";
import { WASI } from "wasi";

//
// const { instantiate } = await import(process.argv[2]);

export const wasi = new WASI({
    args: ["argument1", "аргумент2"],
    env: {
        "PATH" : "/usr/local/bin:/usr/bin",
        "HOME": "/Users/이준"
    },
    preopens: {
        // '/sandbox': '/Actual/dir',
    },
});

const { exports, instance } = await instantiate({ wasi_snapshot_preview1 : wasi.wasiImport });
wasi.initialize(instance);

// Run unit tests if needed
exports?.startUnitTests();

// exports.someOtherJsExportFunction()
