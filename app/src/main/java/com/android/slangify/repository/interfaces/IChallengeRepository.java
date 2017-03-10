package com.android.slangify.repository.interfaces;

import com.android.slangify.repository.models.ChallengeModel;

/**
 * Created by limormekaiten on 3/8/17.
 */

public interface IChallengeRepository {

    //todo - TBD - maybe we want to retrieve also by language ? by vocabulary?
    void getChallengeIDs(IRepositoryCallback callback);

    /**
     * A meme data will include:
     * meme creator
     * creation timestamp
     * chosen phrase id - todo: should we fetch in this call also the vocabulary's data?
     * video path
     * video thumbnail path
     * @param challengeID
     * @param callback
     */
    void getChallengeData(int challengeID, IRepositoryCallback<ChallengeModel> callback);
}
