package com.example.customedit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Пользователь on 29.03.2018.
 */

public class CustomEditText extends AppCompatEditText {

    private Drawable mImage;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){

        mImage = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_clear_black_24dp, null);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {

                if(text.length()!=0){
                    showClearButton();
                }else{
                    hideClearButton();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setOnTouchListener(new OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Use the getCompoundDrawables()[2] expression to check
                // if the drawable is on the "end" of text [2].
                if ((getCompoundDrawablesRelative()[2] != null)) {
                    float clearButtonStart; // Used for LTR languages
                    boolean isClearButtonClicked = false;

                    clearButtonStart = (getWidth() - getPaddingEnd()
                            - mImage.getIntrinsicWidth());

                    // If the touch occurred after the start of the button,
                    // set isClearButtonClicked to true.
                    if (event.getX() > clearButtonStart) {
                        isClearButtonClicked = true;
                    }

                    // Check for actions if the button is tapped.
                    if (isClearButtonClicked) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            getText().clear();
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showClearButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, mImage, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void hideClearButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
    }
}
