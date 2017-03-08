package repository;

/**
 * Created by limormekaiten on 3/8/17.
 */

/**
 * The supported languages repository will have:
 * - supported language ID
 * - supported language name
 * - flag? (optional)
 */
public interface ISupportedLanguagesRepository {


    void getLanguagesIDs(IRepositoryCallback callback);

    void getLanguageName(String id, IRepositoryCallback callback);

}
