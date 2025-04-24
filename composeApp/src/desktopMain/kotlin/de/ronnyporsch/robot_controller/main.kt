package de.ronnyporsch.robot_controller

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "RobotController",
        state = WindowState(size = DpSize(800.dp, 800.dp),)
    ) {
        App()
    }
}