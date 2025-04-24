package de.ronnyporsch.robot_controller.robot_control.domain

import de.ronnyporsch.robot_controller.ROBOT_IP
import de.ronnyporsch.robot_controller.ROBOT_COMMAND_PORT
import de.ronnyporsch.robot_controller.ROBOT_DATA_PORT
import de.ronnyporsch.robot_controller.data_glove_simulation.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder

object Robot {
    private val currentRobotPose = MutableStateFlow(DoubleArray(6))

    fun move(
        jointPositions: List<Double>,
        a: Double,
        v: Double,
        movementType: MovementType,
        callback: ((Result<Unit>) -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val jointPositionsString = jointPositions.joinToString(prefix = "[", postfix = "]", separator = ", ")
                val moveTypeAsString = when (movementType) {
                    MovementType.MOVE_J -> "movej"
                    MovementType.MOVE_P -> "movep"
                    MovementType.MOVE_L -> "movel"
                }
                val command = "$moveTypeAsString($jointPositionsString, a=$a, v=$v)\n"

                Socket(ROBOT_IP, ROBOT_COMMAND_PORT).use { socket ->
                    socket.getOutputStream().use { out ->
                        out.write(command.toByteArray())
                        out.flush()
                    }
                }
                callback?.invoke(Result.success(Unit))
            } catch (e: Exception) {
                e.printStackTrace()
                callback?.invoke(Result.failure(e))
            }
        }
    }

    fun move(frame: Frame) {
        TODO("Not yet implemented")
    }

    suspend fun listenForCurrentPoseContinuously(): Nothing {
        println("starting to listen for robot pose continuously")
        Socket(ROBOT_IP, ROBOT_DATA_PORT).use { socket ->
            socket.getInputStream().use { input ->
                println("Connected via port ${socket.port})")
                val buffer = ByteArray(1108)
                while (true) {
                    val bytesRead = input.read(buffer)
                    if (bytesRead > 0) {
                        val tcpPose = DoubleArray(6)
                        for (i in 0..5) {
                            val offset = 444 + i * 8 // TCP Pose Offset
                            tcpPose[i] = ByteBuffer.wrap(buffer, offset, 8)
                                .order(ByteOrder.BIG_ENDIAN)
                                .getDouble()
                        }
                        currentRobotPose.emit(tcpPose)
                        println("current pose: $tcpPose")
                    }
                }
            }
        }
    }
}

enum class MovementType {
    MOVE_J,
    MOVE_P,
    MOVE_L
}

