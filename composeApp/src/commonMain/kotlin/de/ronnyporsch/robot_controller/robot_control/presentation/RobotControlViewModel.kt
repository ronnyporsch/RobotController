package de.ronnyporsch.robot_controller.robot_control.presentation

import androidx.lifecycle.ViewModel
import de.ronnyporsch.robot_controller.data_glove_simulation.startDataGloveSimulation
import de.ronnyporsch.robot_controller.robot_control.domain.MovementType
import de.ronnyporsch.robot_controller.robot_control.domain.Robot
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RobotControlViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RobotControlUiState())
    val uiState = _uiState.asStateFlow()

    private var gloveSimulationJob: Job? = null
    private val jobMutex = Mutex()

    fun updateJointValue(index: Int, value: String) {
        _uiState.update { state ->
            val newList = state.jointValues.toMutableList().apply { this[index] = value }
            state.copy(jointValues = newList)
        }
    }

    fun updateAcceleration(value: String) {
        _uiState.update { it.copy(acceleration = value) }
    }

    fun updateVelocity(value: String) {
        _uiState.update { it.copy(velocity = value) }
    }

    fun submit() {
        try {
            val state = _uiState.value
            val jointPositions = state.jointValues.mapNotNull { it.toDoubleOrNull() }
            val a = state.acceleration.toDouble()
            val v = state.velocity.toDouble()

            Robot.move(jointPositions, a, v, MovementType.MOVE_L, ::robotMoveCallback)

            _uiState.update { it.copy(errorMessage = null) }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "An error occurred: ${e.message}") }
        }
    }

    fun robotMoveCallback(result: Result<Unit>) {
        result.onFailure { throwable ->
            _uiState.update { it.copy(errorMessage = "${throwable::class.simpleName}:${throwable.message}") }
        }
    }

    fun toggleGloveSimulation() {
        CoroutineScope(Dispatchers.Default).launch {
            jobMutex.withLock {
                if (_uiState.value.gloveSimulationRunning) {
                    gloveSimulationJob?.cancel()
                    gloveSimulationJob = null
                    _uiState.update { it.copy(gloveSimulationRunning = false) }
                } else {
                    gloveSimulationJob = CoroutineScope(Dispatchers.Default).launch {
                        try {
                            _uiState.update { it.copy(gloveSimulationRunning = true) }
                            startDataGloveSimulation()
                        } catch (_: CancellationException) {
                            // Silently handle cancellation
                            _uiState.update { it.copy(gloveSimulationRunning = false) }
                        } catch (e: Exception) {
                            _uiState.update { it.copy(
                                gloveSimulationRunning = false,
                                errorMessage = "Glove simulation error: ${e.message}"
                            ) }
                        } finally {
                            _uiState.update { it.copy(gloveSimulationRunning = false) }
                        }
                    }
                }
            }
        }
    }
}
