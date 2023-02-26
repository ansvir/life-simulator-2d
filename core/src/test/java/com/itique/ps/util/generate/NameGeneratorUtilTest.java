package com.itique.ps.util.generate;

import org.junit.jupiter.api.Test;

public class NameGeneratorUtilTest {

    @Test
    public void testGenerateNameWithDefaultConfig() {
        System.out.println(NameGeneratorUtil.generateName(10));
    }

    @Test
    public void testGenerateNameWithCustomConfig() {
        NameGeneratorUtil.Config config = NameGeneratorUtil.Config.builder()
                .withConsonantChance(25.0)
                .withVowelChance(75.0)
                .capitalize().build();
        System.out.println(NameGeneratorUtil.generateName(10, config));
    }

}
