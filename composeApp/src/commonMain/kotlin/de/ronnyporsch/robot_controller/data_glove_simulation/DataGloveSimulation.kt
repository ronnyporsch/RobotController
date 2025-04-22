package de.ronnyporsch.robot_controller.data_glove_simulation

fun startDataGloveSimulation() {
    val frameGen = SmoothFrameGenerator()
    for (i in 0 .. 999) {
        val frame = frameGen.generateFrame()
        println(frame)
        //TODO send frame to robot
        Thread.sleep(32) //32 ms sleep time to reach roughly 30 fps output
    }
}