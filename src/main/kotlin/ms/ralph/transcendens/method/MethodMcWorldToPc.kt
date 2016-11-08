package ms.ralph.transcendens.method

import ms.ralph.transcendens.DataType.*
import ms.ralph.transcendens.Utils
import ms.ralph.transcendens.asKey
import net.morbz.minecraft.blocks.CustomBlock
import net.morbz.minecraft.blocks.Material
import net.morbz.minecraft.level.FlatGenerator
import net.morbz.minecraft.level.GameType
import net.morbz.minecraft.level.IGenerator
import net.morbz.minecraft.level.Level
import net.morbz.minecraft.world.DefaultLayers
import net.morbz.minecraft.world.World
import org.iq80.leveldb.Options
import org.iq80.leveldb.impl.Iq80DBFactory
import java.io.File

class MethodMcWorldToPc(source: File, destination: String, params: Array<String>) : AbstractMethod(source, destination, params) {
    override fun start() {
        val db = Iq80DBFactory.factory.open(source, Options()) ?: return
        val world = createWorld()
        try {
            db.iterator().forEach {
                if (it.key.size != 9) return@forEach
                it.value ?: return@forEach
                val key = it.key.asKey()
                when (key.third) {
                    TERRAIN_DATA -> processTerrain(world, key.first, key.second, it.value)
                    TILE_ENTITY,
                    ENTITY,
                    ONE_BYTE,
                    UNKNOWN -> {
                    }
                }
            }
        } finally {
            world.save()
            db.close()
        }
    }

    private fun createWorld(): World {
        val layers = DefaultLayers().apply {
            setLayer(0, Material.BEDROCK)
            setLayers(1, 2, Material.DIRT)
            setLayer(3, Material.GRASS)
        }
        val generator: IGenerator = FlatGenerator(layers)
        val level = Level(destination, generator).apply {
            gameType = GameType.CREATIVE
            setSpawnPoint(0, 0, 0)
        }
        return World(level, layers)
    }

    private fun processTerrain(world: World, chunkX: Int, chunkZ: Int, value: ByteArray) {
        val ids = arrayOfNulls<Int>(32768)
        val data = arrayOfNulls<Int>(32768)
        for (i in 0..32767) {
            ids[i] = value[i].toInt() and 0xFF
        }
        for (i in 0..32767 step 2) {
            data[i] = value[32768 + (i / 2)].toInt() and 0xF
            data[i + 1] = value[32768 + (i / 2)].toInt().ushr(4) and 0xF
        }
        var position = 0
        for (i in 0..15) {
            for (j in 0..15) {
                for (y in 0..127) {
                    val x = 16 * chunkX + i
                    val z = 16 * chunkZ + j
                    val id = Utils.fixMcWorldToPcBlockIdConflict(ids[position]) ?: continue
                    val metadata = Utils.fixMcWorldToPcBlockMetadataConflict(id, data[position]) ?: continue
                    world.setBlock(x, y, z, CustomBlock(id, metadata, 0))
                    position += 1
                }
            }
        }
    }
}