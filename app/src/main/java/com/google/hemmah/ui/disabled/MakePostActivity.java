package com.google.hemmah.ui.disabled;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.hemmah.R;
import com.google.hemmah.model.Post;

public class MakePostActivity extends AppCompatActivity {
    private TextView chooseDateTextView;
    private Button requestButton;
    private TextInputLayout titleLayout;
    private TextInputLayout descriptionLayout;
    private Calendar calendar = Calendar.getInstance();
    private Post post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initViews();
        chooseDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateBaker();
            }
        });
    }

    private void initViews() {
        chooseDateTextView = findViewById(R.id.help_choose_date_textView);
        requestButton = findViewById(R.id.help_button_add_request);
        titleLayout = findViewById(R.id.help_title_layout);
        descriptionLayout = findViewById(R.id.help_description_layout);
    }

    private void showDateBaker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);
                        chooseDateTextView.setText("" + dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //    private void createNewPost() {
//        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.7:8080/").
//                addConverterFactory(GsonConverterFactory.create()).build();
//        WebServices apiInterFace = retrofit.create(WebServices.class);
//        Call<String> call = apiInterFace.createPost(post);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Toast.makeText(requireContext(),response.body(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(requireContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
