package com.android.slangify.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.android.slangify.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.android.slangify.repository.implementation.LanguagesRepository;
import com.android.slangify.repository.interfaces.IRepositoryCallback;
import com.android.slangify.repository.models.LanguageModel;
import com.android.slangify.utils.IntentUtils;
import com.devspark.robototextview.widget.RobotoAutoCompleteTextView;
import com.devspark.robototextview.widget.RobotoTextView;

import java.util.ArrayList;


/**
 * Created by limormekaiten on 3/8/17.
 */

public class CreateChallengeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_submit)
    RobotoTextView btnSubmit;
    @BindView(R.id.languages_list)
    RobotoAutoCompleteTextView languagesList;

    private ArrayList<LanguageModel> languageModels;

    private static final int LOCATION_PERMISSION_CODE = 000;
    private static final int STORAGE_PERMISSION_CODE = 111;
    private static final int AUDIO_PERMISSION_CODE = 222;
    private static final int CAMERA_PERMISSION_CODE = 333;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_challenge);
        ButterKnife.bind(this);

        //Request all permissions
        requestFewPermissions(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);


        languagesList.setThreshold(1);

        LanguagesRepository languagesRepository = new LanguagesRepository();
        languagesRepository.getLanguages(new IRepositoryCallback<LanguageModel>() {
            @Override
            public void onSuccess(ArrayList<LanguageModel> result) {
                //save data
                languageModels = result;
                String[] mStringArray = new String[languageModels.size()];
                for (int i = 0; i < languageModels.size(); i++) {
                    mStringArray[i] = languageModels.get(i).getName();
                }

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        CreateChallengeActivity.this, android.R.layout.simple_dropdown_item_1line,
                        mStringArray);

                languagesList.setAdapter(arrayAdapter);
                languagesList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View arg0) {
                        languagesList.showDropDown();
                    }
                });

            }

            @Override
            public void onFailure() {

            }
        });

        setListeners();

    }

    private void setListeners() {
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                //todo - add metadata about the chosen phrase
                IntentUtils.startVideoCaptureActivity(CreateChallengeActivity.this);
                break;
        }
    }

    public void requestFewPermissions(String... permissions) {
        requestPermissions(permissions,19999);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 19999) {
            Log.d("tag", "onRequestPermissionsResult: ");
        }
    }
}
