package de.ronnyporsch.robot_controller.robot_control.domain

import de.ronnyporsch.robot_controller.ROBOT_IP
import de.ronnyporsch.robot_controller.ROBOT_PORT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.Socket


object Robot {
    fun moveJ(jointPositions: List<Double>, a: Double = 1.2, v: Double = 0.25, callback: (Result<Unit>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val jointPositionsString = jointPositions.joinToString(prefix = "[", postfix = "]", separator = ", ")
                val command = "movej($jointPositionsString, a=$a, v=$v)\n"

                Socket(ROBOT_IP, ROBOT_PORT).use { socket ->
                    socket.getOutputStream().use { out ->
                        out.write(command.toByteArray())
                        out.flush()
                    }
                }
                callback(Result.success(Unit))
            } catch (e: Exception) {
                e.printStackTrace()
                callback(Result.failure(e))
            }
        }
    }
}


