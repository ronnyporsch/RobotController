package de.ronnyporsch.robot_controller.data_glove_simulation

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.coroutineScope

suspend fun startDataGloveSimulation() = coroutineScope {
    val frameGen = SmoothFrameGenerator()
    while (isActive) {
        val frame = frameGen.generateFrame()
        println(frame)
        //TODO send frames to robot
        delay(32) // 32 ms delay to reach roughly 30 fps output
    }
}
