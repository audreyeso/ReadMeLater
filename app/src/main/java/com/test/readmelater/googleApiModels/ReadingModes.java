package com.test.readmelater.googleApiModels;

/**
 * Created by audreyeso on 8/9/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReadingModes {

    @SerializedName("text")
    @Expose
    private Boolean text;
    @SerializedName("image")
    @Expose
    private Boolean image;

    /**
     *
     * @return
     * The text
     */
    public Boolean getText() {
        return text;
    }

    /**
     *
     * @param text
     * The text
     */
    public void setText(Boolean text) {
        this.text = text;
    }

    /**
     *
     * @return
     * The image
     */
    public Boolean getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(Boolean image) {
        this.image = image;
    }

}