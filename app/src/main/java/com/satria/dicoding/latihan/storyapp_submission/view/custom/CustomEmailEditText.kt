package com.satria.dicoding.latihan.storyapp_submission.view.custom

import android.content.Context
import android.util.AttributeSet

class CustomEmailEditText : AppCompatValidationEditText {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onValidationTextChanged() {
        if (isValidEmail()) {
            setValidationError(null)
        } else {
            setValidationError("Please input a valid email that contains @web.domain")
        }
    }

    private fun isValidEmail(): Boolean {
        val email = text.toString()
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
