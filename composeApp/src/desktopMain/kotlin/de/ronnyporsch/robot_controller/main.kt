package de.ronnyporsch.robot_controller

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "RobotController",
    ) {
        App()
    }
}