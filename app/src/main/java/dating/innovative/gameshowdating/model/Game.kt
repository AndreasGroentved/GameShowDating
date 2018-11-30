package dating.innovative.gameshowdating.model

data class Game(
    val nonJudger: String,
    val userCount: Int,
    val userLeft: Int,
    val gameId: String,
    val roundNumber: Int
)