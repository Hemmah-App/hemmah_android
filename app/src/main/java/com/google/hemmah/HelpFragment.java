package com.google.hemmah;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.hemmah.api.WebServices;
import com.google.hemmah.model.Post;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.N)
public class HelpFragment extends Fragment {

    TextView chooseDateTextView;
    Button requestButton;
    TextInputLayout titleLayout;
    TextInputLayout descriptionLayout;
    Calendar calendar = Calendar.getInstance();
    Post post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        chooseDateTextView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showDateBaker();
            }
        });
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post = new Post(titleLayout.getEditText().getText().toString(),
                        descriptionLayout.getEditText().getText().toString(),
                        chooseDateTextView.getText().toString());
                createNewPost();
            }
        });
    }

    private void createNewPost() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.7:8080/").
                addConverterFactory(GsonConverterFactory.create()).build();
        WebServices apiInterFace = retrofit.create(WebServices.class);
        Call<String> call = apiInterFace.createPost(post);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(requireContext(),response.body(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(requireContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews(View view) {
        chooseDateTextView = view.findViewById(R.id.help_choose_date_textView);
        requestButton = view.findViewById(R.id.help_button_add_request);
        titleLayout = view.findViewById(R.id.help_title_layout);
        descriptionLayout = view.findViewById(R.id.help_description_layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDateBaker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
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
}