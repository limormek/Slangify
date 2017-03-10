package com.android.slangify.repository.models;

import java.util.Date;

/**
 * Created by bettykin on 10/03/2017.
 */

public class ChallengeModel {

    private String creatorId;
    private String phraseId;
    private String videoPath;
    private String videoThumbnailPath;
    private Date creationDate;

    public ChallengeModel(String creatorId, String phraseId, String videoPath, String videoThumbnailPath, Date creationDate) {
        this.creatorId = creatorId;
        this.phraseId = phraseId;
        this.videoPath = videoPath;
        this.videoThumbnailPath = videoThumbnailPath;
        this.creationDate = creationDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getPhraseId() {
        return phraseId;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getVideoThumbnailPath() {
        return videoThumbnailPath;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
