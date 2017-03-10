package com.android.slangify.repository.interfaces;

/**
 * Created by limormekaiten on 3/8/17.
 */

import com.android.slangify.repository.models.LanguageModel;

/**
 * The supported languages com.android.slangify.repository will have:
 * - supported language ID
 * - supported language name
 * - flag? (optional)
 */
public interface ILanguagesRepository {


    void getLanguages(IRepositoryCallback<LanguageModel> callback);


    void getLanguagesIDs(IRepositoryCallback callback);

    void getLanguageName(String id, IRepositoryCallback callback);

}
