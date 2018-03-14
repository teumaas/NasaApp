package nl.teumaas.nasaapp.api.interfaces;

import nl.teumaas.nasaapp.domain.Photo;

/**
 * Created by tmsmi on 13-3-2018.
 */

public interface OnPhotoAvailable {
    void OnPhotoAvailable(Photo photo);
}
