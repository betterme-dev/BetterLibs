package world.betterme.betterlibs.slack

import com.squareup.moshi.Json
import org.gradle.internal.time.Time

data class SlackMessage(
    val username: String,
    val channel: String,
    @Json(name = "icon_url") val iconUrl: String = "",
    val attachments: List<Attachment>
) {
    data class Attachment(
        val color: String = Color.YELLOW.hexColor,
        val title: String,
        val pretext: String,
        val text: String,
        val footer: String,
        val ts: Long = Time.currentTimeMillis().div(ROUNDING_NUMBER)
    ) {
        enum class Color(val hexColor: String) {
            RED("#c4291c"),
            GREEN("#36a64f"),
            YELLOW("#d6a048"),
        }
    }

    companion object {
        const val ROUNDING_NUMBER = 1000
    }
}
