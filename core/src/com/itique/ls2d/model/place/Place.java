package com.itique.ls2d.model.place;

import com.itique.ls2d.model.Identifiable;

public abstract class Place extends Identifiable {

    private final PlaceType type;

    public Place(PlaceType type) {
        this.type = type;
    }

    public PlaceType getType() {
        return type;
    }

}
