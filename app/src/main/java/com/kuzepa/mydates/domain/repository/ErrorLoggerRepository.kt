package com.kuzepa.mydates.domain.repository

interface ErrorLoggerRepository {
    suspend fun logError(errorMessage: String)
    fun logErrorSync(errorMessage: String)
}