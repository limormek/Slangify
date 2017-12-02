package com.android.slangify;

import android.app.Application;

import java.util.ArrayList;

import com.android.slangify.repository.implementation.ChallengeRepository;
import com.android.slangify.repository.implementation.LanguagesRepository;
import com.android.slangify.repository.implementation.PhraseRepository;
import com.android.slangify.repository.interfaces.IRepositoryCallback;
import com.android.slangify.repository.models.ChallengeModel;
import com.android.slangify.repository.models.LanguageModel;
import com.android.slangify.repository.models.PhraseModel;
import com.android.slangify.utils.SharedPreferencesUtils;

/**
 * Created by bettykin on 09/03/2017.
 */

public class SlangifyApplication extends Application {

    private static ArrayList<LanguageModel> languagesArray = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        // get languages from Firebase database
        LanguagesRepository languagesRepository = new LanguagesRepository();
        languagesRepository.getLanguages(new IRepositoryCallback<LanguageModel>() {
            @Override
            public void onSuccess(ArrayList<LanguageModel> result) {
                SlangifyApplication.languagesArray = result;

                //Save data in Shared Preferences
                SharedPreferencesUtils.setLanguagesCache(SlangifyApplication.this, SlangifyApplication.languagesArray);
            }

            @Override
            public void onFailure() {

            }
        });

        PhraseRepository phrasesRepository = new PhraseRepository();
        phrasesRepository.getPhraseData(1, new IRepositoryCallback<PhraseModel>() {
            @Override
            public void onSuccess(ArrayList<PhraseModel> result) {
                PhraseModel phrase = result.get(0);
            }

            @Override
            public void onFailure() {

            }
        });

        ChallengeRepository challengeRepository = new ChallengeRepository();
        challengeRepository.getChallengeData(0, new IRepositoryCallback<ChallengeModel>() {
            @Override
            public void onSuccess(ArrayList<ChallengeModel> result) {
                ChallengeModel challenge = result.get(0);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public static ArrayList<LanguageModel> getLanguages() {
        return languagesArray;
    }
}
