package de.ronnyporsch.robot_controller.robot_control.presentation

data class RobotControlUiState(
    val jointValues: List<String> = List(6) { "" },
    val acceleration: String = "1.2",
    val velocity: String = "0.25",
    val errorMessage: String? = null
)