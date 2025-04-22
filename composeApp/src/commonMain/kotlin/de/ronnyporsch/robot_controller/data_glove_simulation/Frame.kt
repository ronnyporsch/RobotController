package de.ronnyporsch.robot_controller.data_glove_simulation

data class Frame(val frameNumber: Int, val acceleration: DoubleArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Frame

        if (frameNumber != other.frameNumber) return false
        if (!acceleration.contentEquals(other.acceleration)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = frameNumber.hashCode()
        result = 31 * result + acceleration.contentHashCode()
        return result
    }

}
