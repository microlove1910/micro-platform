package com.micro.thing.test;

import com.micro.thing.AdminApplication;
import com.micro.thing.dao.SysMenuDao;
import com.micro.thing.entity.SysMenuEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = {AdminApplication.class})
public class DaoTest {

    @Resource
    SysMenuDao sysMenuDao;

    @Test
    public void test1() {
        final List<SysMenuEntity> maps = sysMenuDao.loadByAdminId(1);


        int i = 1;
    }

}
