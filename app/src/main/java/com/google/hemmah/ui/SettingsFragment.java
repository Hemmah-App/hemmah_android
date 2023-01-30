package com.google.hemmah.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;

import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.dataManager.PostsAdapter;
import com.google.hemmah.model.Post;

import java.util.ArrayList;
import java.util.zip.Inflater;

import kotlin.reflect.jvm.internal.impl.renderer.DescriptorRenderer;

public class SettingsFragment extends Fragment {
    public static final String mTAG = "com.google.hemmah.ui.SettingsFragment";
    public static final String SHARED_PREFERENCE_FILE_NAME = "Preferences_Values";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences;
        try {
            sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, getContext().MODE_PRIVATE);
            boolean isDarkmode = sharedPreferences.getBoolean()
        }catch (NullPointerException e){
            Log.d(mTAG,e.getMessage());
        }

    }
}
