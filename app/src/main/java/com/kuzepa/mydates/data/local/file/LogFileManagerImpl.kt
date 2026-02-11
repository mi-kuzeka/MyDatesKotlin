package com.kuzepa.mydates.data.local.file

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.kuzepa.mydates.domain.manager.LogFileManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class LogFileManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LogFileManager {

    companion object {
        private const val LOG_FILE_NAME = "log.txt"
        private const val LOG_DIRECTORY_NAME = "logs"
        private const val FILE_PROVIDER_AUTHORITY = ".fileprovider"
    }

    override fun getLogFileUri(): Uri? {
        return try {
            val file = getLogFile()
            if (file.exists()) {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}$FILE_PROVIDER_AUTHORITY",
                    file
                )
            } else {
                null
            }
        } catch (e: Exception) {
            // TODO handle error (show message)
            null
        }
    }

    private fun getLogsDirectory(): File {
        return File(context.filesDir, LOG_DIRECTORY_NAME).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    override fun getLogFile(): File {
        return File(getLogsDirectory(), LOG_FILE_NAME)
    }

    override fun ensureLogFileExists(): File {
        return getLogFile().apply {
            if (!exists()) {
                parentFile?.mkdirs()
                createNewFile()
            }
        }
    }
}