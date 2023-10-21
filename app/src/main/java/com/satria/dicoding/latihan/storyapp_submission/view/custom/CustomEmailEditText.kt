package com.satria.dicoding.latihan.storyapp_submission.view.custom

import android.content.Context
import android.util.AttributeSet
import com.satria.dicoding.latihan.storyapp_submission.R

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
            setValidationError(context.getString(R.string.please_input_a_valid_email_that_contains_web_domain))
        }
    }

    private fun isValidEmail(): Boolean {
        val email = text.toString()
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
