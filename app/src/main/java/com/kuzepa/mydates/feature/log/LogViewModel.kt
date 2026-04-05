package com.kuzepa.mydates.feature.log

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuzepa.mydates.R
import com.kuzepa.mydates.domain.manager.LogFileManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logFileManager: LogFileManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(LogUiState())
    val uiState: StateFlow<LogUiState> = _uiState.asStateFlow()

    init {
        loadLog()
    }

    fun loadLog() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(loadingState = LogLoadingState.Loading)
            }

            val result = runCatching {
                val file = logFileManager.getLogFile()

                when {
                    !file.exists() -> ReadingLogResult.NoFile
                    file.length() == 0L -> ReadingLogResult.EmptyFile
                    else -> {
                        file.readText()
                        ReadingLogResult.Success
                    }
                }
            }

            _uiState.update {
                when {
                    result.isFailure -> it.copy(
                        loadingState = LogLoadingState.Error(
                            message = context.resources.getString(R.string.message_failed_to_load_error_log)
                                    + result.exceptionOrNull()?.message
                        )
                    )

                    result.getOrNull() is ReadingLogResult.NoFile -> it.copy(
                        loadingState = LogLoadingState.NoLog(
                            message = context.resources.getString(R.string.error_log_file_not_found)
                        )
                    )

                    result.getOrNull() is ReadingLogResult.EmptyFile -> it.copy(
                        loadingState = LogLoadingState.NoLog(
                            message = context.resources.getString(R.string.error_log_file_empty)
                        )
                    )

                    else -> it.copy(loadingState = LogLoadingState.Success)
                }
            }
        }
    }

    fun clearLog() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = logFileManager.ensureLogFileExists()
                file.writeText("")
                loadLog()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Clearing failure: ${e.message}"
                    )
                }
            }
        }
    }

    fun resetSharingState() {
        _uiState.update {
            it.copy(sharingEmailIntentState = LogSharingEmailIntentState.Loading)
        }
    }

    fun sendReportWithNoLog(cause: String, errorMessage: String) {
        val emailIntent = getEmailIntent(message = "$cause\nError message: $errorMessage\n")
        createEmailClientChooser(emailIntent)
    }

    fun shareLog() {
        resetSharingState()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = logFileManager.getLogFile()
                if (file.exists()) {
                    withContext(Dispatchers.Main) {
                        val emailIntent = getEmailIntent(uri = logFileManager.getLogFileUri())
                        createEmailClientChooser(emailIntent)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Sending failure: ${e.message}"
                    )
                }
            }
        }
    }

    fun createEmailClientChooser(emailIntent: Intent) {
        val intent =
            Intent.createChooser(
                emailIntent,
                context.resources.getString(R.string.button_send_report)
            )
        if (intent == null) {
            _uiState.update { state ->
                state.copy(
                    sharingEmailIntentState = LogSharingEmailIntentState.Error(
                        context.resources.getString(R.string.email_client_not_found)
                    )
                )
            }
        } else {
            _uiState.update { state ->
                state.copy(
                    sharingEmailIntentState = LogSharingEmailIntentState.Success(intent)
                )
            }
        }
    }

    fun getEmailIntent(uri: Uri? = null, message: String = ""): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            // need this to prompts email client only
            type = "message/rfc822"
            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(context.resources.getString(R.string.support_email))
            )
            putExtra(
                Intent.EXTRA_SUBJECT,
                context.resources.getString(R.string.error_report_mail_subject)
            )
            putExtra(
                Intent.EXTRA_TEXT,
                message.ifBlank {
                    context.resources.getString(R.string.error_report_mail_body_log)
                }
            )
            uri?.let {
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}