package com.android.slangify.repository.implementation;

import com.android.slangify.repository.interfaces.IChallengeRepository;
import com.android.slangify.repository.interfaces.IRepositoryCallback;
import com.android.slangify.repository.models.ChallengeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by bettykin on 09/03/2017.
 */

public class ChallengeRepository implements IChallengeRepository {


    private FirebaseDatabase mDatabase;

    public static final String CHALLENGE_REPOSITORY = "challenges";

    public ChallengeRepository() {
        mDatabase = FirebaseDatabase.getInstance();
    }
    @Override
    public void getChallengeIDs(IRepositoryCallback callback) {

    }

    @Override
    public void getChallengeData(int challengeID, final IRepositoryCallback<ChallengeModel> callback) {
        DatabaseReference phrases = mDatabase.getReference(CHALLENGE_REPOSITORY);
        phrases.child(String.valueOf(challengeID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChallengeModel challenge = dataSnapshot.getValue(ChallengeModel.class);
                ArrayList<ChallengeModel> challenges = new ArrayList<ChallengeModel>();
                challenges.add(challenge);
                callback.onSuccess(challenges);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
