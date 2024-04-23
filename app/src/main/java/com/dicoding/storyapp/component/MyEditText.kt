package com.dicoding.storyapp.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.storyapp.R

class MyEditText: AppCompatEditText, View.OnTouchListener{

    private lateinit var passwordButtonImage: Drawable
    private lateinit var passwordIcon:Drawable
    private var isPasswordVisible = false

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)
        passwordButtonImage = ContextCompat.getDrawable(context, if (!isPasswordVisible) R.drawable.baseline_remove_red_eye_24 else R.drawable.mdi_hide ) as Drawable
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.baseline_lock_outline_24) as Drawable
        setEditCompoundDrawables(endOfTheText = passwordButtonImage, startOfTheText = passwordIcon)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing.
                if (s.toString().length < 8) {
                    setError(resources.getString(R.string.minimum_characters), null)
                } else {
                    error = null
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })

    }

    private fun setEditCompoundDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if(compoundDrawables[2] != null){
            val passwordButtonStart: Float
            val passwordButtonEnd: Float
            var isPasswordButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                passwordButtonEnd = (passwordButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                        event.x < passwordButtonEnd -> isPasswordButtonClicked = true
                    }
            } else {
                passwordButtonStart = (width - paddingEnd - passwordButtonImage.intrinsicWidth).toFloat()
                when {
                        event.x > passwordButtonStart -> isPasswordButtonClicked = true
                    }
            }
            if(isPasswordButtonClicked){
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        passwordButtonImage = ContextCompat.getDrawable(context, if(isPasswordVisible) R.drawable.mdi_hide else R.drawable.baseline_remove_red_eye_24) as Drawable
                        setEditCompoundDrawables(endOfTheText = passwordButtonImage, startOfTheText = passwordIcon)
                        transformationMethod = if(isPasswordVisible) null else PasswordTransformationMethod.getInstance()
                        isPasswordVisible = !isPasswordVisible
                    }
                }
                return false
            }
            return false
        }
        return false
    }
}
