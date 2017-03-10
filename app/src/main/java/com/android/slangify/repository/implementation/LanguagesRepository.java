package com.android.slangify.repository.implementation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import com.android.slangify.repository.interfaces.IRepositoryCallback;
import com.android.slangify.repository.interfaces.ILanguagesRepository;
import com.android.slangify.repository.models.LanguageModel;

/**
 * Created by bettykin on 09/03/2017.
 */

public class LanguagesRepository implements ILanguagesRepository {

    private FirebaseDatabase mDatabase;

    public static final String LANGUAGE_REPOSITORY = "languages";

    public LanguagesRepository() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void getLanguages(final IRepositoryCallback<LanguageModel> callback) {
        DatabaseReference languages = mDatabase.getReference(LANGUAGE_REPOSITORY);
        languages.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<LanguageModel> languages = new ArrayList<LanguageModel>();
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    languages.add(new LanguageModel((String) next.getValue(), Integer.valueOf(next.getKey())));
                }
                callback.onSuccess(languages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void getLanguagesIDs(IRepositoryCallback callback) {

    }

    @Override
    public void getLanguageName(String id, IRepositoryCallback callback) {

    }
}
