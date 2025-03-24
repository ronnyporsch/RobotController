package de.ronnyporsch.robot_controller

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import de.ronnyporsch.robot_controller.robot_control.presentation.RobotControlScreen
import de.ronnyporsch.robot_controller.robot_control.presentation.RobotControlViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        RobotControlScreen(RobotControlViewModel())
    }
}