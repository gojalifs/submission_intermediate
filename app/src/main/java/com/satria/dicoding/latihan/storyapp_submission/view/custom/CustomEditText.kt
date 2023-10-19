package com.satria.dicoding.latihan.storyapp_submission.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.satria.dicoding.latihan.storyapp_submission.R

open class AppCompatValidationEditText : AppCompatEditText {
    private lateinit var errorIcon: Drawable
    private lateinit var roundedBackground: Drawable
    private var validationError: String? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = roundedBackground
        if (validationError != null) {
            showErrorIcon()
        } else {
            showErrorIcon(false)
        }
    }

    private fun init() {
        errorIcon = ContextCompat.getDrawable(context, R.drawable.round_error_24) as Drawable
        roundedBackground =
            ContextCompat.getDrawable(context, R.drawable.rounded_edit_text) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onValidationTextChanged()
            }

            override fun afterTextChanged(s: Editable?) {
                if (validationError != null) {
                    setError(validationError, null)
                } else {
                    error = null
                }
            }
        })
    }

    open fun onValidationTextChanged() {
        // Implement your validation logic in subclasses
    }

    private fun showErrorIcon(isError: Boolean = true) {
        if (isError) {
            setButtonDrawables(endOfTheText = errorIcon)
        } else {
            setButtonDrawables()
        }
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }

    fun setValidationError(errorMessage: String?) {
        validationError = errorMessage
    }
}
