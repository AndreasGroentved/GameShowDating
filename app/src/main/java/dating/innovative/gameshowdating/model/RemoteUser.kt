package dating.innovative.gameshowdating.model

data class RemoteUser(
    val _id: String,
    val password: String,
    val profilePicture: ByteArray,
    val biography: String,
    val sex: String,
    val age: Int
)