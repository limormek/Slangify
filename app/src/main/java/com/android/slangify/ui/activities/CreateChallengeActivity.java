package com.android.slangify.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.android.slangify.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.android.slangify.repository.implementation.LanguagesRepository;
import com.android.slangify.repository.implementation.PhraseRepository;
import com.android.slangify.repository.interfaces.IRepositoryCallback;
import com.android.slangify.repository.models.LanguageModel;
import com.android.slangify.repository.models.PhraseModel;
import com.android.slangify.utils.IntentUtils;
import com.devspark.robototextview.widget.RobotoAutoCompleteTextView;
import com.devspark.robototextview.widget.RobotoTextView;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by limormekaiten on 3/8/17.
 */

public class CreateChallengeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_submit)
    RobotoTextView btnSubmit;
    @BindView(R.id.languages_list)
    RobotoAutoCompleteTextView languagesList;

    private ArrayList<LanguageModel> languageModels;
    private PhraseModel selectdPhrase;

    private static final int LOCATION_PERMISSION_CODE = 000;
    private static final int STORAGE_PERMISSION_CODE = 111;
    private static final int AUDIO_PERMISSION_CODE = 222;
    private static final int CAMERA_PERMISSION_CODE = 333;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_challenge);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Request all permissions
            requestFewPermissions(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }

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
                        CreateChallengeActivity.this, R.layout.layout_spinner_dropdown_item,
                        mStringArray);

                languagesList.setAdapter(arrayAdapter);
                languagesList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View arg0) {
                        languagesList.showDropDown();
                    }
                });

                languagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        LanguageModel selectedLanguage = languageModels.get(position);
                        PhraseRepository phraseRepository = new PhraseRepository();
                        phraseRepository.getPhraseData(selectedLanguage.getId(), new IRepositoryCallback<PhraseModel>() {
                            @Override
                            public void onSuccess(ArrayList<PhraseModel> result) {
                                if (result != null && result.size() > 0) {
                                    // Start camera activity with random Phrase
                                    Random r = new Random();
                                    int randomNumber = r.nextInt(result.size() -1) +1;
                                    selectdPhrase = result.get(randomNumber);
                                }
                            }

                            @Override
                            public void onFailure() {
                                // Tell user there was an error
                            }
                        });
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
                IntentUtils.startVideoCaptureActivity(CreateChallengeActivity.this, selectdPhrase);
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
