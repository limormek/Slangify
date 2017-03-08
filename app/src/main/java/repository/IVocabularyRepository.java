package repository;

/**
 * Created by limormekaiten on 3/8/17.
 */

public interface IVocabularyRepository {

    void getVocabularyIDs(String languageID, IRepositoryCallback callback);

    /**
     * A vocabulary data will contain:
     * language id
     * vocabulary text (the foreign sentence to pronounce)
     * translation
     * nice degree (optional - TBD) //todo TBD
     * did you know (optional - TBD) //todo TBD
     * @param vocabularyID
     * @param callback
     */
    void getVocabularyData(String vocabularyID, IRepositoryCallback callback);
}
