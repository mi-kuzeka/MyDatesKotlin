package com.kuzepa.mydates.data.repository

import android.util.Log
import com.kuzepa.mydates.domain.manager.LogFileManager
import com.kuzepa.mydates.domain.repository.ErrorLoggerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileErrorLoggerRepository @Inject constructor(
    private val logFileManager: LogFileManager
) : ErrorLoggerRepository {

    private companion object {
        private const val MAX_LOG_SIZE = 1024 * 1024 // 1MB
    }

    override suspend fun logError(errorMessage: String) {
        withContext(Dispatchers.IO) {
            try {
                val file = logFileManager.ensureLogFileExists()
                file.appendText(errorMessage)
                rotateIfNeeded()
            } catch (e: IOException) {
                Log.e("FileErrorLogger", "Failed to write log: ${e.message}")
            }
        }
    }

    override fun logErrorSync(errorMessage: String) {
        CoroutineScope(Dispatchers.IO).launch {
            logError(errorMessage)
        }
    }

    private fun rotateIfNeeded() {
        val file = logFileManager.getLogFile()
        if (file.length() > MAX_LOG_SIZE) {
            val lines = file.readLines()
            val half = lines.size / 2
            file.writeText(lines.drop(half).joinToString("\n"))
        }
    }
}