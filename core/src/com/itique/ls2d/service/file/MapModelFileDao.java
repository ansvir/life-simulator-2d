package com.itique.ls2d.service.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.itique.ls2d.mapeditor.model.BuildingModel;
import com.itique.ls2d.mapeditor.model.MapModel;
import com.itique.ls2d.mapeditor.model.RoadModel;
import com.itique.ls2d.model.world.BuildingType;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.itique.ls2d.constant.Constant.MAPS_DATA_DIR;

public class MapModelFileDao implements FileDao<MapModel> {

    private static final String ID = "Id";
    private static final String MAP = "Map";
    private static final String NAME = "Name";
    private static final String BUILDINGS = "Buildings";
    private static final String BUILDING = "Building";
    private static final String TYPE = "type";
    private static final String X_START = "XStart";
    private static final String Y_START = "YStart";
    private static final String WIDTH = "Width";
    private static final String HEIGHT = "Height";
    private static final String ROADS = "Roads";
    private static final String ROAD = "Road";
    private static final String MAP_IMAGE_BASE_64 = "MapImageBase64";

    @Override
    public void save(MapModel mapModel) {
        try (Writer writer = Gdx.files.local(MAPS_DATA_DIR + "/"
                + getFileName(mapModel.getName())).writer(false);
             XmlWriter xmlWriter = new XmlWriter(writer)) {
            xmlWriter.element(MAP)
                    .element(ID).text(mapModel.getId()).pop()
                    .element(NAME).text(mapModel.getName()).pop()
                    .element(WIDTH).text(mapModel.getWidth()).pop()
                    .element(HEIGHT).text(mapModel.getHeight()).pop()
                    .element(BUILDINGS);
            for (int i = 0; i < mapModel.getBuildings().size(); i++) {
                BuildingModel building = mapModel.getBuildings().get(i);
                xmlWriter.element(BUILDING)
                        .attribute(TYPE, building.getType().name())
                        .element(X_START).text(building.getX()).pop()
                        .element(Y_START).text(building.getY()).pop()
                        .element(WIDTH).text(building.getWidth()).pop()
                        .element(HEIGHT).text(building.getHeight()).pop().pop();
            }
            xmlWriter.pop();
            xmlWriter.element(ROADS);
            for (int i = 0; i < mapModel.getRoads().size(); i++) {
                RoadModel road = mapModel.getRoads().get(i);
                xmlWriter.element(ROAD)
                        .attribute(TYPE, road.getType().name())
                        .element(X_START).text(road.getX()).pop()
                        .element(Y_START).text(road.getY()).pop()
                        .element(WIDTH).text(road.getWidth()).pop()
                        .element(HEIGHT).text(road.getHeight()).pop().pop();
            }
            xmlWriter.pop();
            xmlWriter.element(MAP_IMAGE_BASE_64).text(mapModel.getMapImageBase64())
                    .pop();
            xmlWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(String id) {
        findAll().stream().filter(m -> m.getId().equals(id))
                        .forEach(m -> Gdx.files.local(MAPS_DATA_DIR + "/"
                                + getFileName(m.getName())).delete());
    }

    @Override
    public void deleteAll() {
        Gdx.files.local(MAPS_DATA_DIR).delete();
    }

    @Override
    public Optional<MapModel> findById(String id) {
        return findAll().stream().filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<MapModel> findAll() {
        XmlReader xmlReader = new XmlReader();
        return Arrays.stream(Gdx.files.local(MAPS_DATA_DIR + "/").list()).map(f -> {
            XmlReader.Element root = xmlReader.parse(f);
            String id = root.getChildByName(ID).getText();
            String name = root.getChildByName(NAME).getText();
            float width = Float.parseFloat(root.getChildByName(WIDTH).getText());
            float height = Float.parseFloat(root.getChildByName(HEIGHT).getText());
            List<BuildingModel> buildings = Arrays.stream(root.getChildByName(BUILDINGS)
                    .getChildrenByName(BUILDING).toArray(XmlReader.Element.class)).map(b ->
                        new BuildingModel(
                                BuildingType.valueOf(b.getAttribute(TYPE)),
                                b.getFloat(X_START),
                                b.getFloat(Y_START),
                                b.getFloat(WIDTH),
                                b.getFloat(HEIGHT))).collect(Collectors.toList());
            List<RoadModel> roads = Arrays.stream(root.getChildByName(ROADS)
                    .getChildrenByName(ROAD).toArray(XmlReader.Element.class)).map(r ->
                    new RoadModel(
                            r.getFloat(X_START),
                            r.getFloat(Y_START),
                            r.getFloat(WIDTH),
                            r.getFloat(HEIGHT),
                            RoadModel.Type.valueOf(r.getAttribute(TYPE))))
                    .collect(Collectors.toList());
            String mapImageBase64 = root.getChildByName(MAP_IMAGE_BASE_64).getText();
            return new MapModel(id, name, width, height, roads, buildings, mapImageBase64);
        }).collect(Collectors.toList());
    }

    private static String getFileName(String mapName) {
        return mapName.toLowerCase().replace(" ", "_") + ".xml";
    }

}
