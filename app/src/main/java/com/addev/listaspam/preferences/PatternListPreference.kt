package com.addev.listaspam.preferences

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.Toast
import androidx.preference.EditTextPreference
import com.addev.listaspam.R

class PatternListPreference : EditTextPreference {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setOnPreferenceChangeListener { preference, newValue ->
            if (validatePatterns(newValue as String)) {
                true
            } else {
                Toast.makeText(
                    preference.context,
                    R.string.pref_pattern_list_error,
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        }
    }

    private fun validatePatterns(input: String): Boolean {
        if (TextUtils.isEmpty(input)) return true

        val minLen = 3
        val maxLen = 20

        val patterns = input.split("\n")

        for (pattern in patterns) {
            val trimmed = pattern.trim()
            if (trimmed.isEmpty()) continue

            // Check min and max length per line
            if (trimmed.length < minLen || trimmed.length > maxLen) {
                return false
            }

            // Only allow numbers, +, and *
            if (!trimmed.matches(Regex("^[0-9+*]+$"))) {
                return false
            }

            // Do not allow consecutive **
            if (trimmed.contains("**")) {
                return false
            }

            // Do not allow consecutive ++
            if (trimmed.contains("++")) {
                return false
            }
        }

        return true
    }
}
