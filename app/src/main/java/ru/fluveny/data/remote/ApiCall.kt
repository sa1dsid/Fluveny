package ru.fluveny.data.remote

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import ru.fluveny.domain.model.Resource
import java.io.IOException

suspend fun <T> safeApiCall(block: suspend () -> Response<T>): Resource<T> {
    return try {
        val response = block()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null || response.code() == 204) {
                @Suppress("UNCHECKED_CAST")
                Resource.Success(body as T)
            } else {
                Resource.Error("Пустой ответ сервера")
            }
        } else {
            Resource.Error(parseApiError(response))
        }
    } catch (e: IOException) {
        Resource.Error("Проверьте подключение к интернету", e)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Неизвестная ошибка", e)
    }
}

fun parseApiError(response: Response<*>): String {
    val fallback = "Ошибка сервера: ${response.code()}"
    val error = response.errorBody()?.string().orEmpty()
    if (error.isBlank()) return fallback
    return runCatching {
        val obj = Gson().fromJson(error, JsonElement::class.java).asJsonObject
        obj["message"]?.asString
            ?: obj["error"]?.asString
            ?: obj["detail"]?.asString
            ?: fallback
    }.getOrDefault(fallback)
}

inline fun <reified T> Gson.parseListEnvelope(json: JsonElement?): List<T> {
    if (json == null || json.isJsonNull) return emptyList()
    val element = when {
        json.isJsonArray -> json
        json.isJsonObject && json.asJsonObject.has("content") -> json.asJsonObject["content"]
        json.isJsonObject && json.asJsonObject.has("items") -> json.asJsonObject["items"]
        json.isJsonObject && json.asJsonObject.has("data") -> json.asJsonObject["data"]
        else -> json
    }
    if (!element.isJsonArray) return emptyList()
    val type = object : TypeToken<List<T>>() {}.type
    return fromJson(element, type)
}
