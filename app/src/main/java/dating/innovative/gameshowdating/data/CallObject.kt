package dating.innovative.gameshowdating.data

import java.util.*

data class CallObject(val method: String, val data: String, val id: String = UUID.randomUUID().toString())