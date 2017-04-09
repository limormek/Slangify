package com.android.slangify.ui.activities;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import com.android.slangify.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.android.slangify.repository.implementation.LanguagesRepository;
import com.android.slangify.repository.implementation.PhraseRepository;
import com.android.slangify.repository.interfaces.IRepositoryCallback;
import com.android.slangify.repository.models.LanguageModel;
import com.android.slangify.repository.models.PhraseModel;
import com.android.slangify.utils.IntentUtils;
import com.android.slangify.utils.UiUtils;
import com.android.slangify.utils.Utils;
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
    RobotoAutoCompleteTextView mLanguageAutoComplete;

    @BindView(R.id.rude_seekbar)
    SeekBar mRudeSeekbar;

    private ArrayList<LanguageModel> languageModels;
    private PhraseModel selectedPhrase;

    private static final int LOCATION_PERMISSION_CODE = 000;
    private static final int STORAGE_PERMISSION_CODE = 111;
    private static final int AUDIO_PERMISSION_CODE = 222;
    private static final int CAMERA_PERMISSION_CODE = 333;
    private LanguageModel selectedLanguage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_challenge);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Request all permissions
            requestFewPermissions(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }

        mLanguageAutoComplete.setThreshold(1);

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

                mLanguageAutoComplete.setAdapter(arrayAdapter);
                mLanguageAutoComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View arg0) {
                        mLanguageAutoComplete.showDropDown();
                    }
                });

                mLanguageAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedLanguage = languageModels.get(position);
                        fetchPhrase(false);
                    }
                });
            }

            @Override
            public void onFailure() {
                //todo check internet
            }
        });

        setListeners();

    }

    private void fetchPhrase(final boolean shouldSubmit) {
        PhraseRepository phraseRepository = new PhraseRepository();
        phraseRepository.getPhraseData(selectedLanguage.getId(), new IRepositoryCallback<PhraseModel>() {
            @Override
            public void onSuccess(ArrayList<PhraseModel> result) {
                if (result != null) {
                    if(result.size() == 1) {
                        selectedPhrase = result.get(0);
                    } else if (result.size() > 1) {
                        // Start camera activity with random Phrase
                        Random r = new Random();
                        int randomNumber = r.nextInt(result.size() - 1) + 1;
                        selectedPhrase = result.get(randomNumber);
                    }
                }
                UiUtils.hideKeyboard(mLanguageAutoComplete);
                if(shouldSubmit) {
                    startCaptureActivity();
                }
            }

            @Override
            public void onFailure() {
                // Tell user there was an error
            }
        });
    }

    private void setListeners() {
        btnSubmit.setOnClickListener(this);

        mLanguageAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LanguageModel langModel = isLanguageSupported(s.toString());
                if(langModel != null) {
                    selectedLanguage = langModel;
                    fetchPhrase(false);
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                submitChallenge();
                break;
        }
    }

    private void submitChallenge() {
        if(selectedPhrase == null) {

            //language is not supported
            String language = mLanguageAutoComplete.getText().toString();

            if(TextUtils.isEmpty(language)){
                Utils.makeSafeToast(CreateChallengeActivity.this, R.string.error_language_not_chosen);
                return;
            }

            selectedLanguage = isLanguageSupported(language);
            if (selectedLanguage != null) {
                fetchPhrase(true);
            } else {
                Utils.makeSafeToast(CreateChallengeActivity.this, R.string.error_language_not_supported);
            }

        } else {
            startCaptureActivity();
        }
    }

    private void startCaptureActivity() {
        IntentUtils.startVideoCaptureActivity(CreateChallengeActivity.this, selectedPhrase, selectedLanguage.getName());
    }

    /**
     * Checks if the language is supported.
     * If so - returns the relevant language model (in the database)
     * returns null otherwise
     * @param language
     * @return
     */
    private LanguageModel isLanguageSupported(String language) {
        if (languageModels != null) {
            for (LanguageModel lang :
                    languageModels) {
                if (lang.getName().equalsIgnoreCase(language)) {
                    return lang;
                }
            }
        }

        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Permissions
    ///////////////////////////////////////////////////////////////////////////
    public void requestFewPermissions(String... permissions) {
        requestPermissions(permissions, 19999);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 19999) {
            Log.d("tag", "onRequestPermissionsResult: ");
        }
    }
}
