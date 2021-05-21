package com.moviepocket.restclient

class Result<T> private constructor(private val value: T?, private val error: Throwable?) {
    suspend fun onSuccess(action: suspend (value: T) -> Any): Result<T> {
        value?.let { action(it) }
        return this
    }

    suspend fun onFailure(action: suspend (Throwable) -> Unit): Result<T> {
        error?.let { action(it) }
        return this
    }

    suspend fun onAny(action: suspend (Result<T>) -> Unit): Result<T> {
        value?.let { action(this) }
        error?.let { action(this) }
        return this
    }

    suspend fun <V> flatMap(action: suspend (T) -> Result<V>): Result<V> {
        if (value == null) return Result(value, error)
        return action(value)
    }

    suspend fun <V> map(action: suspend (T) -> V): Result<V> {
        if (value == null) return Result(value, error)
        return success(action(value))
    }

    suspend fun mapThrowable(action: suspend (Throwable) -> Throwable): Result<T> {
        if (error == null) return this
        return failure(action(error))
    }

    fun getOrDefault(defaultValue: T): Result<T> {
        return if (value == null) success(defaultValue) else this
    }

    fun getOrNull(): T? = value

    fun errorOrNull(): Throwable? = error

    companion object {
        fun <T> success(data: T) = Result(value = data, error = null)
        fun <T> failure(error: Throwable) = Result<T>(value = null, error = error)
    }
}