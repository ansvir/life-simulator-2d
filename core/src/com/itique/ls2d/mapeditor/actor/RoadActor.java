package com.itique.ls2d.mapeditor.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.itique.ls2d.constant.RGBAConstant;
import com.itique.ls2d.mapeditor.model.RoadModel;

import static com.itique.ls2d.constant.Constant.ROAD_HEIGHT;
import static com.itique.ls2d.constant.Constant.ROAD_WIDTH;

public class RoadActor extends AbstractGroup {
    
    private ShapeRenderer renderer;
    private RoadModel roadModel;
    
    public RoadActor(ShapeRenderer renderer, RoadModel roadModel) {
        super(new Vector2(roadModel.getX(), roadModel.getY()),
                new Vector2(roadModel.getWidth(), roadModel.getHeight()));
        this.renderer = renderer;
        this.roadModel = roadModel;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        if (roadModel.getType() == RoadModel.Type.STRAIGHT) {
            System.out.println("STRAIGHT");
            FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int) roadModel.getWidth(), (int) roadModel.getHeight(), false);
            frameBuffer.begin();
            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.rect(roadModel.getX(), roadModel.getY(), roadModel.getWidth(), roadModel.getHeight());
            frameBuffer.end();
            batch.draw(frameBuffer.getColorBufferTexture(), 0, 0);
        }
        renderer.end();
    }

    public RoadModel getRoadModel() {
        return roadModel;
    }

}
