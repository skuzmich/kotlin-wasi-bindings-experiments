package kotlinx.wasi

import kotlin.test.*

class WasiTests {
    @Test
    fun Test_args_sizes_get() {
        val (size, size_buffer) = args_sizes_get()
        assertEquals(size, 2)
        assertEquals(size_buffer, 28)
    }

    @Test
    fun Test_args_get() {
        val args: List<String> = args_get()
        assertEquals(args, listOf("argument1", "аргумент2"))
    }

    @Test
    fun Test_environ_sizes_get() {
        val (size, size_buffer) = environ_sizes_get()
        assertEquals(size, 2)
        assertEquals(size_buffer, 48)
    }

    @Test
    fun Test_environ_get() {
        val environ: Map<String, String> = environ_get()
        assertEquals(
            environ,
            mapOf(
                "PATH" to "/usr/local/bin:/usr/bin",
                "HOME" to "/Users/이준"
            )
        )
    }

    @Test
    fun testPrintln() {
        val nl = "\n".encodeToByteArray()
        fun wasiPrintln(x: Any?) {
            fd_write(1, listOf(x.toString().encodeToByteArray(), nl))
        }

        for (arg in environ_get().entries) {
            wasiPrintln(arg)
        }

        repeat(6) { fd ->
            try {
                wasiPrintln(fd_prestat_get(fd))
                wasiPrintln(fd_prestat_dir_name(fd))
            } catch (e: WasiError) {
                wasiPrintln("WASI error: $e")
            }
        }
    }
}