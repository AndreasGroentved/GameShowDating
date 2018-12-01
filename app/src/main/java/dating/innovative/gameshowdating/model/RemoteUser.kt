package dating.innovative.gameshowdating.model

data class RemoteUser(
    val _id: String,
    val password: String,
    val profilePicture: ByteArray?,
    val biography: String,
    val sex: String,
    val age: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RemoteUser

        if (_id != other._id) return false

        return true
    }

    override fun hashCode(): Int {
        return _id.hashCode()
    }
}