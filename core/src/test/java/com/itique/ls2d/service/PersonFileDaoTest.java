package com.itique.ls2d.service;

import com.itique.ls2d.util.generate.PersonGeneratorUtil;

public class PersonFileDaoTest {

//    @Test
    public void testSave() {
        new PersonFileDao().save(PersonGeneratorUtil.generatePerson());
    }
}
