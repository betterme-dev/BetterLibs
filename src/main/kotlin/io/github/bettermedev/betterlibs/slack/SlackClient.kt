package io.github.bettermedev.betterlibs.slack

import com.squareup.moshi.JsonAdapter
import okhttp3.Response
import io.github.bettermedev.betterlibs.Adapters.adapter
import io.github.bettermedev.betterlibs.BaseClient
import io.github.bettermedev.betterlibs.BaseClient.Companion.AUTHORIZATION

class SlackClient(
    private val token: String,
    private val slackMessage: SlackMessage
) : BaseClient {

    private val moshiAdapter: JsonAdapter<SlackMessage> = adapter()

    override fun run() {
        postMessage()
    }

    private fun postMessage() {
        val (response, status) = makePostRequest()
        val isSuccessful = response?.isSuccessful ?: return
        if (!isSuccessful) {
            error("Could not create slack message: $status ${response.message}\n")
        }
    }

    private fun makePostRequest(): Pair<Response?, Int> {
        val json = moshiAdapter.toJson(slackMessage)
        return postRequest(
            json,
            "https://slack.com/api/chat.postMessage",
            AUTHORIZATION,
            "Bearer $token"
        )
    }
}
