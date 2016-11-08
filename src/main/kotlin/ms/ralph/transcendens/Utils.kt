package ms.ralph.transcendens

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun ByteArray.asKey(): Triple<Int, Int, DataType> {
    val x = ByteBuffer.wrap(copyOfRange(0, 4)).order(ByteOrder.LITTLE_ENDIAN).int
    val z = ByteBuffer.wrap(copyOfRange(4, 8)).order(ByteOrder.LITTLE_ENDIAN).int
    val type = DataType.parse(get(8))
    return Triple(x, z, type)
}

object Utils {

    fun fixMcWorldToPcBlockIdConflict(mcWorldId: Int?): Int? {
        return when (mcWorldId) {
            157 -> 125 // Double Wooden Slab
            158 -> 126 // Wooden Slab
            else -> mcWorldId
        }
    }

    fun fixMcWorldToPcBlockMetadataConflict(mcWorldId: Int?, metadata: Int?): Int? {
        if (mcWorldId == 44 && metadata == 14) {
            return 15 // Upper Quartz Slab
        }
        return metadata
    }
}