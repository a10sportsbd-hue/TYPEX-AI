package com.typex.ai.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class OnlineAiProvider(
    private val endpoint: String,
    private val apiKey: String,
    private val model: String
) : AiProvider {

    override suspend fun process(request: AiRequest): AiResult = withContext(Dispatchers.IO) {
        if (endpoint.isBlank() || apiKey.isBlank() || model.isBlank()) {
            throw AiFailure.Configuration("Endpoint, API key and model are required.")
        }
        val normalizedEndpoint = endpoint.trim().removeSuffix("/")
        val url = URL(
            if (normalizedEndpoint.endsWith("/chat/completions")) normalizedEndpoint
            else "$normalizedEndpoint/chat/completions"
        )
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 15_000
            readTimeout = 45_000
            doOutput = true
            setRequestProperty("Authorization", "Bearer ${apiKey.trim()}")
            setRequestProperty("Content-Type", "application/json")
        }

        try {
            val messages = JSONArray()
                .put(JSONObject().put("role", "system").put("content", request.action.instruction))
                .put(JSONObject().put("role", "user").put("content", request.text))

            val payload = JSONObject()
                .put("model", model.trim())
                .put("messages", messages)
                .put("temperature", 0.3)

            connection.outputStream.use { it.write(payload.toString().toByteArray(Charsets.UTF_8)) }
            val status = connection.responseCode
            val stream = if (status in 200..299) connection.inputStream else connection.errorStream
            val body = stream?.bufferedReader()?.use { it.readText() }.orEmpty()

            if (status !in 200..299) {
                throw AiFailure.Network("AI provider returned HTTP $status. Check endpoint, model and API access.")
            }

            val response = JSONObject(body)
            val content = response.optJSONArray("choices")
                ?.optJSONObject(0)
                ?.optJSONObject("message")
                ?.optString("content")
                ?.trim()
                .orEmpty()

            if (content.isBlank()) throw AiFailure.InvalidResponse("The AI provider returned an empty response.")
            AiResult(content, "Online AI", false)
        } catch (failure: AiFailure) {
            throw failure
        } catch (failure: Exception) {
            throw AiFailure.Network("Could not connect to the AI provider. Check your internet and endpoint.", failure)
        } finally {
            connection.disconnect()
        }
    }
}
