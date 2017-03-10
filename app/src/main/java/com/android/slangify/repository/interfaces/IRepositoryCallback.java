package com.android.slangify.repository.interfaces;

import java.util.ArrayList;

/**
 * Created by limormekaiten on 3/8/17.
 */

public interface IRepositoryCallback<T> {

    void onSuccess(ArrayList<T> result);

    void onFailure();
}
