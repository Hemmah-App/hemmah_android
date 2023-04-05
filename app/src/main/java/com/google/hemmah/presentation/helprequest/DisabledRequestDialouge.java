package com.google.hemmah.presentation.helprequest;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.hemmah.R;
import com.google.hemmah.domain.model.HelpRequest;

import dagger.hilt.android.processor.internal.viewmodel.ViewModelProcessor;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DisabledRequestDialouge extends Fragment {
    private ImageView mBackImageView;
    private Button mPostButton;
    private String mToken;
    private EditText mPopupTitleEditText, mPopupDescriptionEditText, mPopupMeetinglocationEditText;
    private HelpRequestDialogeViewModel mHelpRequestDialogeViewModel;
    private Context mContext;

    public DisabledRequestDialouge(String token) {
        mToken = token;
        mHelpRequestDialogeViewModel = new ViewModelProvider(this).get(HelpRequestDialogeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_request_dialoge, container, false);
        mContext = getContext();
        intializeViews(view);
        handleButtonClicks();
        return view;
    }

    private void intializeViews(View view) {
        mBackImageView = view.findViewById(R.id.addRequest_popup_back_IV);
        mPostButton = view.findViewById(R.id.addRequest_popup_post_BT);
        mPopupTitleEditText = view.findViewById(R.id.popup_title_ET);
        mPopupDescriptionEditText = view.findViewById(R.id.popup_description_ET);
        mPopupMeetinglocationEditText = view.findViewById(R.id.popup_meetinglocation_ET);
    }

    private void handleButtonClicks() {
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHelpRequestAttributes();
                mHelpRequestDialogeViewModel.createHelpRequest(mToken, mHelpRequestDialogeViewModel.getHelpRequest())
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(res -> {
                            if (res.code() == 200) {
                                Toast.makeText(mContext, res.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Failed to create this request", Toast.LENGTH_SHORT).show();
                            }
                        }, err -> {
                            Toast.makeText(mContext, R.string.failedtoconnect_toastmessage, Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    private void getHelpRequestAttributes() {
        String title =mPopupTitleEditText.getText().toString();
        String description = mPopupDescriptionEditText.getText().toString();
        String meetingLocation = mPopupMeetinglocationEditText.getText().toString();
    }

}
