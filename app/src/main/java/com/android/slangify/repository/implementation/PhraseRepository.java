package com.android.slangify.repository.implementation;

import android.util.Log;

import com.android.slangify.repository.interfaces.IPhraseRepository;
import com.android.slangify.repository.interfaces.IRepositoryCallback;
import com.android.slangify.repository.models.PhraseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by bettykin on 09/03/2017.
 */

public class PhraseRepository implements IPhraseRepository {

    private static final String TAG = PhraseRepository.class.getName();
    private FirebaseDatabase mDatabase;

    public static final String PHRASE_REPOSITORY = "phrases";


    public PhraseRepository() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void getVocabularyIDs(String languageID, IRepositoryCallback callback) {

    }

    @Override
    public void getPhraseData(int languageID, final IRepositoryCallback<PhraseModel> callback) {
        DatabaseReference phrases = mDatabase.getReference(PHRASE_REPOSITORY);
        phrases.child(String.valueOf(languageID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<PhraseModel> phrases = new ArrayList<PhraseModel>();
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    phrases.add(next.getValue(PhraseModel.class));
                }
                callback.onSuccess(phrases);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+databaseError.getDetails());
            }
        });
    }
}
