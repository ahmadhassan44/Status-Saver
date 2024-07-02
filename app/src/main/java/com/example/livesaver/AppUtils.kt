package com.example.livesaver

import java.util.Locale

class AppUtils {
    private val languages= arrayOf("System","English","Spanish","French","German","Italian","Turkish","Chinese","Urdu","Portuguese")
    fun getLanguages(): Array<String> {
        return languages
    }

    fun getLanguageDrawableId(language: String): Int {
        return when (language.lowercase(Locale.ROOT)) {
            "english" -> R.drawable.english
            "spanish" -> R.drawable.spanish
            "french" -> R.drawable.french
            "german" -> R.drawable.german
            "italian" -> R.drawable.italian
            "turkish" -> R.drawable.turkish
            "chinese" -> R.drawable.chinese
            "urdu" -> R.drawable.urdu
            "portuguese" -> R.drawable.portuguese
            else -> {
                R.drawable.baseline_settings_24
            }
        }
    }


}