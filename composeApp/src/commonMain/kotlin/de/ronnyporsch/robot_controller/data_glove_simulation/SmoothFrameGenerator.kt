package de.ronnyporsch.robot_controller.data_glove_simulation

import java.util.Random
import kotlin.math.max
import kotlin.math.min

/**
 * Creates frames. Each generated frame is similar to the last,
 * except every 30th frame which introduces a non-smooth random jump.
 */
class SmoothFrameGenerator {
    private var currentFrameNumber = 0
    private val random = Random()
    private val current = doubleArrayOf(
        random.nextDouble(), random.nextDouble(), random.nextDouble()
    )

    fun generateFrame(): Frame {
        if (currentFrameNumber % 30 == 0 && currentFrameNumber != 0) {
            // Sudden jump every 30th frame
            current[0] = random.nextDouble()
            current[1] = random.nextDouble()
            current[2] = random.nextDouble()
        } else {
            // Small smooth random delta
            val dx = (random.nextDouble() - 0.5) * 0.05
            val dy = (random.nextDouble() - 0.5) * 0.05
            val dz = (random.nextDouble() - 0.5) * 0.05

            current[0] = clamp(current[0] + dx)
            current[1] = clamp(current[1] + dy)
            current[2] = clamp(current[2] + dz)
        }

        return Frame(currentFrameNumber++, current.clone())
    }

    companion object {
        private fun clamp(value: Double): Double {
            return max(0.0, min(1.0, value))
        }
    }
}
