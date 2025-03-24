package de.ronnyporsch.robot_controller

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform