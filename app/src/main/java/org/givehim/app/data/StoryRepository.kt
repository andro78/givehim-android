package org.givehim.app.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.givehim.app.BuildConfig
import org.givehim.app.model.Story
import org.givehim.app.model.StoryDraft
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class StoryRepository {
    suspend fun stories(): List<Story> = withContext(Dispatchers.IO) {
        val connection = open("/api/stories", "GET")
        try {
            check(connection.responseCode in 200..299) { "사연을 불러오지 못했습니다." }
            val array = JSONArray(connection.inputStream.bufferedReader().use { it.readText() })
            List(array.length()) { index -> array.getJSONObject(index).toStory() }
        } finally { connection.disconnect() }
    }

    suspend fun submit(draft: StoryDraft): String = withContext(Dispatchers.IO) {
        val connection = open("/api/story-submissions", "POST").apply {
            setRequestProperty("Content-Type", "application/json")
            doOutput = true
        }
        try {
            val body = JSONObject().apply {
                put("category", draft.category); put("title", draft.title); put("nickname", draft.nickname)
                put("contact", draft.contact); put("story", draft.story); put("helpNeeded", draft.helpNeeded)
                put("consent", draft.consent); put("website", "")
            }
            connection.outputStream.use { it.write(body.toString().toByteArray()) }
            val stream = if (connection.responseCode in 200..299) connection.inputStream else connection.errorStream
            val response = JSONObject(stream.bufferedReader().use { it.readText() })
            check(connection.responseCode in 200..299) { if (response.optString("error") == "rate_limited") "한 시간에 최대 3건까지 접수할 수 있습니다." else "접수 내용을 확인해주세요." }
            response.getString("id")
        } finally { connection.disconnect() }
    }

    private fun open(path: String, method: String) = (URL(BuildConfig.API_BASE_URL + path).openConnection() as HttpURLConnection).apply {
        requestMethod = method; connectTimeout = 10_000; readTimeout = 15_000; setRequestProperty("Accept", "application/json")
    }

    private fun JSONObject.toStory() = Story(getString("id"), getString("title"), getString("summary"), getString("nickname"), getString("category"), getString("publishedAt"))
}
