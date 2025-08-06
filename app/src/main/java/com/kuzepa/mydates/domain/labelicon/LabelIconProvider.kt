package com.kuzepa.mydates.domain.labelicon

import com.kuzepa.mydates.R

private const val MIN_ICON_NUMBER = 1
private const val MAX_ICON_NUMBER = 69

fun getIconType(code: Int): IconType =
    when (code) {
        -1 -> IconType.NO_ICON
        0 -> IconType.FIRST_LETTER
        in MIN_ICON_NUMBER..MAX_ICON_NUMBER -> IconType.ICON
        else -> IconType.NO_ICON
    }

fun getIconResIdByCode(code: Int): Int? {
    if (code < MIN_ICON_NUMBER || code > MAX_ICON_NUMBER) return null
    return when (code) {
        1 -> R.drawable.label_ic_favorite
        2 -> R.drawable.label_ic_star
        3 -> R.drawable.label_ic_flag_2
        4 -> R.drawable.label_ic_diamond
        5 -> R.drawable.label_ic_cake
        6 -> R.drawable.label_ic_celebration
        7 -> R.drawable.label_ic_bomb
        8 -> R.drawable.label_ic_handshake
        9 -> R.drawable.label_ic_school
        10 -> R.drawable.label_ic_work
        11 -> R.drawable.label_ic_personal_bag
        12 -> R.drawable.label_ic_construction
        13 -> R.drawable.label_ic_science
        14 -> R.drawable.label_ic_menu_book
        15 -> R.drawable.label_ic_key
        16 -> R.drawable.label_ic_emoji_objects
        17 -> R.drawable.label_ic_music_note
        18 -> R.drawable.label_ic_spoke
        19 -> R.drawable.label_ic_phone_enabled
        20 -> R.drawable.label_ic_smartphone
        21 -> R.drawable.label_ic_desktop_mac
        22 -> R.drawable.label_ic_mail
        23 -> R.drawable.label_ic_stadia_controller
        24 -> R.drawable.label_ic_casino
        25 -> R.drawable.label_ic_sports_and_outdoors
        26 -> R.drawable.label_ic_ice_skating
        27 -> R.drawable.label_ic_exercise
        28 -> R.drawable.label_ic_self_care
        29 -> R.drawable.label_ic_luggage
        30 -> R.drawable.label_ic_local_bar
        31 -> R.drawable.label_ic_coffee
        32 -> R.drawable.label_ic_cloud
        33 -> R.drawable.label_ic_electric_bolt
        34 -> R.drawable.label_ic_clear_day
        35 -> R.drawable.label_ic_wb_twilight
        36 -> R.drawable.label_ic_waves
        37 -> R.drawable.label_ic_eco
        38 -> R.drawable.label_ic_filter_vintage
        39 -> R.drawable.label_ic_deceased
        40 -> R.drawable.label_ic_spa
        41 -> R.drawable.label_ic_nature
        42 -> R.drawable.label_ic_forest
        43 -> R.drawable.label_ic_hive
        44 -> R.drawable.label_ic_pets
        45 -> R.drawable.label_ic_camping
        46 -> R.drawable.label_ic_family_home
        47 -> R.drawable.label_ic_cottage
        48 -> R.drawable.label_ic_location_city
        49 -> R.drawable.label_ic_church
        50 -> R.drawable.label_ic_family_history
        51 -> R.drawable.label_ic_hub
        52 -> R.drawable.label_ic_public
        53 -> R.drawable.label_ic_rocket_launch
        54 -> R.drawable.label_ic_travel
        55 -> R.drawable.label_ic_directions_car
        56 -> R.drawable.label_ic_directions_bike
        57 -> R.drawable.label_ic_sports_gymnastics
        58 -> R.drawable.label_ic_self_improvement
        59 -> R.drawable.label_ic_family_restroom
        60 -> R.drawable.label_ic_diversity_4
        61 -> R.drawable.label_ic_group
        62 -> R.drawable.label_ic_person
        63 -> R.drawable.label_ic_smart_toy
        64 -> R.drawable.label_ic_face
        65 -> R.drawable.label_ic_face_3
        66 -> R.drawable.label_ic_sentiment_very_satisfied
        67 -> R.drawable.label_ic_sentiment_satisfied
        68 -> R.drawable.label_ic_sentiment_dissatisfied
        69 -> R.drawable.label_ic_do_not_disturb
        else -> null
    }
}
