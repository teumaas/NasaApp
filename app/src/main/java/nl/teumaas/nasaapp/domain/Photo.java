package nl.teumaas.nasaapp.domain;

import java.io.Serializable;

/**
 * Created by Tom Smits on 13-3-2018.
 */

public class Photo implements Serializable {

    public String ID;
    public String cameraName;
    public String robotName;
    public String photoDate;
    public String totalPhotos;
    public String imgURL;

    public Photo(String ID, String cameraName, String robotName, String photoDate, String totalPhotos, String imgURL) {
        this.ID = ID;
        this.cameraName = cameraName;
        this.robotName = robotName;
        this.photoDate = photoDate;
        this.totalPhotos = totalPhotos;
        this.imgURL = imgURL;
    }

    public static class PhotoBuilder {

        public String ID;
        public String cameraName;
        public String robotName;
        public String imageURL;
        public String imageDate;
        public String imageTotal;

        public PhotoBuilder(String ID, String cameraName, String robotName, String imageDate, String imageTotal, String imageURL) {
            this.ID = ID;
            this.cameraName = cameraName;
            this.robotName = robotName;
            this.imageDate = imageDate;
            this.imageTotal = imageTotal;
            this.imageURL = imageURL;
        }

        public PhotoBuilder setID(String ID) {
            this.ID = "Image ID: " + ID;
            return this;
        }

        public PhotoBuilder setCameraName(String cameraName) {
            this.cameraName = cameraName;
            return this;
        }

        public PhotoBuilder setRobotName(String robotName) {
            this.robotName = robotName;
            return this;
        }

        public PhotoBuilder setImageURL(String imageURL) {
            this.imageURL = imageURL;
            return this;
        }

        public PhotoBuilder setImageInformation(String imageDate, String imageTotal) {
            this.imageDate = imageDate;
            this.imageTotal = imageTotal;
            return this;
        }

        public Photo build() {
            return new Photo(ID, cameraName, robotName, imageDate, imageTotal, imageURL);
        }

    }
}
