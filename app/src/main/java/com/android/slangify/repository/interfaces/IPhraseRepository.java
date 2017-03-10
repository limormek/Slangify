package com.android.slangify.repository.interfaces;

import com.android.slangify.repository.models.PhraseModel;

/**
 * Created by limormekaiten on 3/8/17.
 */

public interface IPhraseRepository {

    void getVocabularyIDs(String languageID, IRepositoryCallback callback);

    /**
     * A vocabulary data will contain:
     * language id
     * vocabulary text (the foreign sentence to pronounce)
     * translation
     * nice degree (optional - TBD) //todo TBD
     * did you know (optional - TBD) //todo TBD
     * @param languageID
     * @param callback
     */
    void getPhraseData(int languageID, IRepositoryCallback<PhraseModel> callback);
}
