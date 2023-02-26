package com.itique.ls2d.constant.world;

public enum DefaultCityFactory {

    GREEN_ISLAND_CITY {
        @Override
        public DefaultCity getCity() {
            return new DefaultGreenCity();
        }
    };

    public abstract DefaultCity getCity();
}
