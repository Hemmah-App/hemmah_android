package com.google.hemmah.view.disabled;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.hemmah.R;

public class DisabledRequestDialouge extends Dialog {
    private ImageView mBackImageView;
    private Button mPostButton;
    public DisabledRequestDialouge(@NonNull Context context) {
        super(context);
    }

    public DisabledRequestDialouge(@NonNull Context context, int themeResId) {
        super(context, themeResId);

    }

    protected DisabledRequestDialouge(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intializeViews();
        handleButtonClicks();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void intializeViews() {
        setContentView(R.layout.add_request_popup);
        mBackImageView = findViewById(R.id.addRequest_popup_back_IV);
        mPostButton = findViewById(R.id.addRequest_popup_post_BT);
    }

    private void handleButtonClicks() {
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
