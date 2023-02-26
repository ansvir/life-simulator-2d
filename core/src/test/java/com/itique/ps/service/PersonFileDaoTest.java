package com.itique.ps.service;

import com.itique.ps.util.generate.PersonGeneratorUtil;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

public class PersonFileDaoTest {

//    @Test
    public void testSave() {
        new PersonFileDao().save(PersonGeneratorUtil.generatePerson());
    }
}
