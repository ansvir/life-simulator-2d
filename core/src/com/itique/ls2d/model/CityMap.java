package com.itique.ls2d.model;

import com.badlogic.gdx.graphics.Pixmap;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Deprecated(since = "Will be used in future releases")
public class CityMap {

    private String cityId;
    private Pixmap image;

    public CityMap(String cityId, Pixmap image) {
        this.cityId = cityId;
        this.image = image;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public Pixmap getImage() {
        return image;
    }

    public void setImage(Pixmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("cityId", cityId)
                .append("image", image)
                .toString();
    }

}
