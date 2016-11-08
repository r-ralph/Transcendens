package ms.ralph.transcendens

enum class DataType(val value: Byte) {
    TERRAIN_DATA(0x30),
    TILE_ENTITY(0x31),
    ENTITY(0x32),
    ONE_BYTE(0x76),
    UNKNOWN(0x00);

    companion object {
        fun parse(value: Byte): DataType {
            return when (value) {
                TERRAIN_DATA.value -> TERRAIN_DATA
                TILE_ENTITY.value -> TILE_ENTITY
                ENTITY.value -> ENTITY
                ONE_BYTE.value -> ONE_BYTE
                else -> UNKNOWN
            }
        }
    }
}