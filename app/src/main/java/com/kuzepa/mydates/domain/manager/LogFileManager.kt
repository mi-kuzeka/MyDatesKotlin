package com.kuzepa.mydates.domain.manager

import android.net.Uri
import java.io.File

interface LogFileManager {
    fun getLogFileUri(): Uri?
    fun getLogFile(): File
    fun ensureLogFileExists(): File
}