package com.kuzepa.mydates.ui.navigation

object NavigationResult {
    const val OK = 1
    const val CANCEL = 0
    const val EVENT_TYPE_KEY = "event_type_navigation_result"
    const val EVENT_TYPE_ID_KEY = "event_type_id"
    const val LABEL_KEY = "label_navigation_result"
    const val LABEL_ID_KEY = "label_id"
}

data class NavigationResultData(
    val result: Int?,
    val id: String? = null
)