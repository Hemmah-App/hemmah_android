package com.google.hemmah.presentation.common.common;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.hemmah.R;
import com.google.hemmah.domain.model.WalkthroughItem;
import com.google.hemmah.presentation.registration.LoginActivity;

import java.util.ArrayList;

public class WalkthroughActivity extends AppCompatActivity  {
    private ArrayList<WalkthroughItem> walkthroughItems;
    private FloatingActionButton mNextFab;
    private ViewPager2 mWalkthroughViewPager;
    private TabLayout tabLayout;
    private AppAdapter mWalkthroughAdapter;
    private TextView mSkipTextView;
    private Button mGetStartedButton,mBackButton;
    public static final String  titleSlide1 = "Welcome to Hemmah!";
    public static final String  descriptionSlide1 = "This app is designed to help people with disabilities to get the assistance they need,\nLet's get started";
    public static final String  titleSlide2 = "Do you have visual impairment?";
    public static final String  descriptionSlide2 = "Hemmah allows you to make a video call with a volunteer who can assist you with your daily tasks";
    public static final String  titleSlide3 = "Struggling to do a task and Need help?";
    public static final String  descriptionSlide3 = " Hemmah allows you to create a request describing your needs and post it so that volunteers can respond and offer help";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        intializeViews();
        settingUpViewPagerAdapter();
        handlingClicks();
        attachTabLayoutWithViewPager();

    }

    private void attachTabLayoutWithViewPager() {
        new TabLayoutMediator(tabLayout, mWalkthroughViewPager, true, true,
                (tab, position) -> {
                }).attach();
    }

    private void handlingClicks() {
        mNextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWalkthroughViewPager.beginFakeDrag();
                mWalkthroughViewPager.fakeDragBy(-800f);
                mWalkthroughViewPager.endFakeDrag();
            }

        });
        mSkipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               gotoLogin();
            }
        });
        mGetStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogin();
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWalkthroughViewPager.beginFakeDrag();
                mWalkthroughViewPager.fakeDragBy(800f);
                mWalkthroughViewPager.endFakeDrag();
            }
        });
        mWalkthroughViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    mBackButton.setVisibility(View.INVISIBLE);
                }
                else if(position  == walkthroughItems.size()-1) {
                    mNextFab.setVisibility(View.INVISIBLE);
                    mBackButton.setVisibility(View.VISIBLE);
                    mGetStartedButton.setVisibility(View.VISIBLE);
                }
                else{
                    mNextFab.setVisibility(View.VISIBLE);
                    mBackButton.setVisibility(View.VISIBLE);
                    mGetStartedButton.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void gotoLogin() {
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();

        // Proceed to the main activity
        Intent intent = new Intent(WalkthroughActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void settingUpViewPagerAdapter() {
        walkthroughItems = new ArrayList<>();
        walkthroughItems.add(new WalkthroughItem(R.drawable.walkthrough_hi_ic, titleSlide1,descriptionSlide1));
        walkthroughItems.add(new WalkthroughItem(R.drawable.walkthrough_videohelp_ic, titleSlide2, descriptionSlide2));
        walkthroughItems.add(new WalkthroughItem(R.drawable.walkthrough_helprequest_ic, titleSlide3, descriptionSlide3));
        mWalkthroughAdapter = new AppAdapter(walkthroughItems,R.layout.walkthrougth_item,null);
        mWalkthroughViewPager.setAdapter(mWalkthroughAdapter);
    }

    private void intializeViews() {
        mWalkthroughViewPager = findViewById(R.id.viewPager2);
        tabLayout = findViewById(R.id.walkthrough_indicator_TABLAYOUT);
        mNextFab = findViewById(R.id.walkthrough_next_BT);
        mSkipTextView = findViewById(R.id.walkthrough_skik_TV);
        mGetStartedButton = findViewById(R.id.walkthrough_getstarted_BT);
        mBackButton =  findViewById(R.id.walkthrough_back_BT);
    }

}