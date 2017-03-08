package repository;

/**
 * Created by limormekaiten on 3/8/17.
 */

public interface IMemesRepository {

    //todo - TBD - maybe we want to retrieve also by language ? by vocabulary?
    void getMemesIDs(IRepositoryCallback callback);

    /**
     * A meme data will include:
     * meme creator
     * creation timestamp
     * chosen vocabulary id - todo: should we fetch in this call also the vocabulary's data?
     * video path
     * video thumbnail path
     * @param memeID
     * @param callback
     */
    void getMemeData(String memeID, IRepositoryCallback callback);
}
