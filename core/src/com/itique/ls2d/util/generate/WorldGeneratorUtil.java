package com.itique.ls2d.util.generate;

import com.itique.ls2d.model.Man;
import com.itique.ls2d.model.world.City;
import com.itique.ls2d.model.world.World;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.random;

public class WorldGeneratorUtil {

    @Deprecated(since = "Will be used in future releases")
    public static World generateWorld() {
        NameGeneratorUtil.Config nameConfig = NameGeneratorUtil.Config
                .builder().capitalize().build();
        List<City> cities = IntStream.range(50, (int) (random() * 200 + 50)).boxed()
                .map(i -> CityGeneratorUtil.generateCity())
                .collect(Collectors.toList());
        List<Man> humans = cities.stream().flatMap(c -> IntStream.range(1, c.getPopulation()).boxed()
                .map(n -> PersonGeneratorUtil.generatePerson())).collect(Collectors.toList());
        return new World(
                NameGeneratorUtil.generateName(7, nameConfig),
                (int) (random() * 20000 + 10000),
                cities.stream().map(City::getId).collect(Collectors.toList()),
                humans.stream().map(Man::getId).collect(Collectors.toList()));
    }

    public static World generateEmptyWorld() {
        NameGeneratorUtil.Config nameConfig = NameGeneratorUtil.Config
                .builder().capitalize().build();
        return new World(
                NameGeneratorUtil.generateName(7, nameConfig),
                (int) (random() * 20000 + 10000),
                List.of(), List.of());
    }

}
