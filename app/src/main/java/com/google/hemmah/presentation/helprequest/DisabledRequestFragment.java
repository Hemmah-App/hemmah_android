package com.google.hemmah.presentation.helprequest;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.hemmah.R;
import com.google.hemmah.Utils.BasicUtils;
import com.google.hemmah.domain.model.HelpRequest;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@AndroidEntryPoint
public class DisabledRequestFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    @Inject
    HelpRequestDialogeViewModel mHelpRequestDialogeViewModel;
    private ImageView mBackImageView;
    private Button mPostButton;
    private String mToken;
    private View mAddHelpRequestFragmentShadowView;
    private EditText mPopupTitleEditText, mPopupDescriptionEditText, mPopupMeetinglocationEditText;
    private Fragment mThisFragment = this;
    private Context mContext;
    private static final int PERMISSION_REQUEST_CODE = 1;


    public DisabledRequestFragment(String token) {
        mToken = token;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_request_dialoge, container, false);
        intializeViews(view);
        mContext = getContext();
        handleButtonClicks();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View parentView = view.getRootView();
        if (parentView != null) {
            mAddHelpRequestFragmentShadowView = parentView.findViewById(R.id.addRequest_shadow_VIEW);
            mAddHelpRequestFragmentShadowView.setVisibility(View.VISIBLE);
        }


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
                mAddHelpRequestFragmentShadowView.setVisibility(View.GONE);
                removeRequestFragment();
            }
        });
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHelpRequestAttributes();
                HelpRequest helpRequest = mHelpRequestDialogeViewModel.getHelpRequest();
                if (helpRequest != null) {
                    mHelpRequestDialogeViewModel.createHelpRequest(mToken, helpRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(res -> {
                                if (res.code() == 200) {
                                    Toast.makeText(mContext, res.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "Failed to create this request", Toast.LENGTH_SHORT).show();
                                    Log.d("HelpRequest", String.valueOf(res.code()));
                                }
                            }, err -> {
                                Log.d("HelpRequest", err.getMessage());
                                Toast.makeText(mContext, R.string.failedtoconnect_toastmessage, Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }

    private void removeRequestFragment() {
        getParentFragmentManager().beginTransaction().remove(mThisFragment).commit();
    }

    private boolean validFeilds() {
        Boolean isValid = true;
        String title = mPopupTitleEditText.getText().toString();
        String description = mPopupDescriptionEditText.getText().toString();
        String meetingLocation = mPopupMeetinglocationEditText.getText().toString();
        if (title.isEmpty()) {
            mPopupTitleEditText.setError("Please Enter A Title");
        } else {
            mPopupTitleEditText.setError(null);
        }
        if (description.isEmpty()) {
            isValid = false;
            mPopupDescriptionEditText.setError("Please Enter A Description");
        } else {
            mPopupDescriptionEditText.setError(null);
        }

        if (meetingLocation.isEmpty()) {
            isValid = false;
            mPopupMeetinglocationEditText.setError("Please Enter A Meeting Location");
        } else {
            mPopupMeetinglocationEditText.setError(null);
        }
        return isValid;
    }

    private void getHelpRequestAttributes() {
        if (validFeilds()) {
            String title = mPopupTitleEditText.getText().toString();
            String description = mPopupDescriptionEditText.getText().toString();
            String meetingLocation = mPopupMeetinglocationEditText.getText().toString();
            String date = BasicUtils.getCurrentDate();
            mHelpRequestDialogeViewModel.setHelpRequest(new HelpRequest(title,
                    description, date, meetingLocation, 30.28660344661843, 31.28660344661843));
        }
    }


}
