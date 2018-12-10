package dating.innovative.gameshowdating.model

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

data class Game(
    val nonJudger: String,
    val userCount: Int,
    val userLeft: Int,
    val gameId: String,
    val roundNumber: Int
)