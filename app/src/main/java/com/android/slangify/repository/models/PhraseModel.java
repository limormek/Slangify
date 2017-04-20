package com.android.slangify.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bettykin on 09/03/2017.
 */

public class PhraseModel implements Parcelable {
    private String text;
    private String extra;
    private int risky;
    private int score;
    private String translation;


    public PhraseModel() {

    }
    
    public PhraseModel(Parcel in) {

        // the order needs to be the same as in writeToParcel() method
        this.text = in.readString();
        this.extra = in.readString();
        this.score = in.readInt();
        this.risky = in.readInt();
        this.translation = in.readString();
    }

    public PhraseModel(String text, String extra, int score, int risky, String translation) {
        this.text = text;
        this.extra = extra;
        this.score = score;
        this.risky = risky;
        this.translation = translation;
    }


    public static final Creator<PhraseModel> CREATOR = new Creator<PhraseModel>() {
        @Override
        public PhraseModel createFromParcel(Parcel in) {
            return new PhraseModel(in);
        }

        @Override
        public PhraseModel[] newArray(int size) {
            return new PhraseModel[size];
        }
    };

    public String getText() {
        return text;
    }

    public String getExtra() {
        return extra;
    }

    public int getScore() {
        return score;
    }

    public String getTranslation() {
        return translation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(extra);
        dest.writeInt(score);
        dest.writeInt(risky);
        dest.writeString(translation);
    }
}
