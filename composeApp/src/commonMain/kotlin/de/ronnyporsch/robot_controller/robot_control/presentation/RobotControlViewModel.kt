package de.ronnyporsch.robot_controller.robot_control.presentation

import androidx.lifecycle.ViewModel
import de.ronnyporsch.robot_controller.robot_control.domain.Robot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RobotControlViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RobotControlUiState())
    val uiState = _uiState.asStateFlow()

    fun updateJointValue(index: Int, value: String) {
        val newList = _uiState.value.jointValues.toMutableList().apply { this[index] = value }
        _uiState.value = _uiState.value.copy(jointValues = newList)
    }

    fun updateAcceleration(value: String) {
        _uiState.value = _uiState.value.copy(acceleration = value)
    }

    fun updateVelocity(value: String) {
        _uiState.value = _uiState.value.copy(velocity = value)
    }

    fun submit() {
        try {
            val jointPositions = _uiState.value.jointValues.mapNotNull { it.toDoubleOrNull() }
            val a = _uiState.value.acceleration.toDouble()
            val v = _uiState.value.velocity.toDouble()

            Robot.moveJ(jointPositions, a, v, ::robotMoveCallback)

            _uiState.value = _uiState.value.copy(errorMessage = null)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(errorMessage = "An error occurred: ${e.message}")
        }
    }

    fun robotMoveCallback(result: Result<Unit>) {
        result.onFailure { throwable ->
            _uiState.update { it.copy(errorMessage = "${throwable::class.simpleName}:${throwable.message}") }
        }
    }
}