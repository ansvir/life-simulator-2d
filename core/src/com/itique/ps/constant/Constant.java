package com.itique.ps.constant;

public abstract class Constant {
    // prefs
    public static final String GLOBAL_PREF = "global";
    public static final String FIRST_PLAY_KEY = "FIRST_PLAY";
    public static final String HERO_ID_KEY = "HERO_ID";
    public static final String HERO_START_CITY_ID_KEY = "START_CITY_ID";

    // data
    public static final String WORLDS_DATA = "data/worlds.json";
    public static final String HUMANS_DATA = "data/humans.json";
    public static final String CITIES_DATA = "data/cities.json";

    // textures
    // it is supposed that 10 = 1 meter
    public static final int TILE_WIDTH = 10;
    public static final int TILE_HEIGHT = 10;

    // world
    public static final int VISIBLE_WIDTH = 640;
    public static final int VISIBLE_HEIGHT = 480;
}
