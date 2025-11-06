package com.kuzepa.mydates.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kuzepa.mydates.R

enum class ZodiacSign(id: Int, @DrawableRes val iconRes: Int?, @StringRes name: Int?) {
    ARIES(1, R.drawable.zodiac_aries, R.string.zodiac_aries),
    TAURUS(2, R.drawable.zodiac_taurus, R.string.zodiac_taurus),
    GEMINI(3, R.drawable.zodiac_gemini, R.string.zodiac_gemini),
    CANCER(4, R.drawable.zodiac_cancer, R.string.zodiac_cancer),
    LEO(5, R.drawable.zodiac_leo, R.string.zodiac_leo),
    VIRGO(6, R.drawable.zodiac_virgo, R.string.zodiac_virgo),
    LIBRA(7, R.drawable.zodiac_libra, R.string.zodiac_libra),
    SCORPIO(8, R.drawable.zodiac_scorpio, R.string.zodiac_scorpio),
    SAGITTARIUS(9, R.drawable.zodiac_sagittarius, R.string.zodiac_sagittarius),
    CAPRICORN(10, R.drawable.zodiac_capricorn, R.string.zodiac_capricorn),
    AQUARIUS(11, R.drawable.zodiac_aquarius, R.string.zodiac_aquarius),
    PISCES(12, R.drawable.zodiac_pisces, R.string.zodiac_pisces),
    UNKNOWN(0, null, null)
}