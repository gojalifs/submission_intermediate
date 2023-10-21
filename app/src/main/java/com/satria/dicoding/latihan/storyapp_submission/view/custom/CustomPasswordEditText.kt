package com.satria.dicoding.latihan.storyapp_submission.view.custom

import android.content.Context
import android.util.AttributeSet
import com.satria.dicoding.latihan.storyapp_submission.R

class CustomPasswordEditText : AppCompatValidationEditText {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onValidationTextChanged() {
        if (text.toString().length >= 8) {
            setValidationError(null)
        } else {
            setValidationError(context.getString(R.string.password_must_be_more_than_8_characters))
        }
    }
}
