/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package kotlinx.wasi

import kotlin.wasm.unsafe.*

class WasiError(val error: Any?)
    : Throwable(message = "WASI call failed with $error")

// TODO: Do not encode twice :)
internal val String.size: Int
    get() = encodeToByteArray().size

const val PTR_SIZE = 4

internal fun readZeroTerminatedString(ptr: Int, byteArray: ByteArray): Int {
    for (i in 0 until byteArray.size) {
        val b = loadByte(ptr + i)
        if (b.toInt() == 0)
            return i
        byteArray[i] = b
    }
    error("Zero-terminated string is out of bounds")
}

internal fun MemoryAllocator.writeToLinearMemory(array: ByteArray): Int {
    val ptr = allocate(array.size)
    var currentPtr = ptr
    for (el in array) {
        storeByte(currentPtr, el)
        currentPtr++
    }
    return ptr
}

internal fun MemoryAllocator.writeToLinearMemory(array: __unsafe__IovecArray): Int {
    val ptr = allocate(array.size * 8)
    var currentPtr = ptr
    for (el in array) {
        __store___unsafe__Iovec(el, currentPtr)
        currentPtr += 8
    }
    return ptr
}

internal fun MemoryAllocator.writeToLinearMemory(array: __unsafe__CiovecArray): Int {
    val ptr = allocate(array.size * 8)
    var currentPtr = ptr
    for (el in array) {
        __store___unsafe__Ciovec(el, currentPtr)
        currentPtr += 8
    }
    return ptr
}

internal fun MemoryAllocator.writeToLinearMemory(string: String): Int =
    writeToLinearMemory(string.encodeToByteArray())

data class LinearMemoryString(val addr: Pointer, val size: Int)

internal fun MemoryAllocator.toMem(s: String): LinearMemoryString {
    val array = s.encodeToByteArray()
    return LinearMemoryString(writeToLinearMemory(array), array.size)
}

internal fun loadByteArray(addr: Int, size: Int): ByteArray =
    ByteArray(size) { i -> loadByte(addr + i) }

internal fun loadString(addr: Int, size: Int): String {
    val bytes = loadByteArray(addr, size)
    val endIndex =
        if (size != 0 && bytes[size - 1] == 0.toByte())
            size - 1  // skip last 0 for 0-terminated strings
        else
            size

    return bytes.decodeToString(endIndex = endIndex)
}
