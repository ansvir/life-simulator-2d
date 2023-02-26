package com.itique.ls2d.util.validator;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.itique.ls2d.control.KeyboardControl;
import com.itique.ls2d.model.world.Terrain;
import com.itique.ls2d.util.generate.TerrainGeneratorUtil;

public class HumanValidatorUtil {

    public static void validate(KeyboardControl control, Texture cityMapTexture) {
        if (control.isUp()) {
            
        }
    }

    private static boolean validateSolid(int x, int y, Texture texture) {
        texture.getTextureData().prepare();
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        if (TerrainGeneratorUtil.getTerrainFromPixel(pixmap.getPixel(x, y)) == Terrain.WATER) {
            pixmap.dispose();
            return true;
        }
        return false;
    }

}
