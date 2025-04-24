package de.ronnyporsch.robot_controller.data_glove_simulation

import de.ronnyporsch.robot_controller.robot_control.domain.Robot
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.coroutineScope

suspend fun startDataGloveSimulation() = coroutineScope {
    val frameGen = SmoothFrameGenerator()
    while (isActive) {
        val frame = frameGen.generateFrame()
        println(frame)
        Robot.move(frame)
        delay(32) // 32 ms delay to reach roughly 30 fps output
    }
}
