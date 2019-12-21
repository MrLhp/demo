package com.example.demo;

import com.example.demo.bean.PersonBean;
import com.example.demo.service.DataHelperService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class,PersonBean.class,DataHelperService.class})
public class DataHelperTest {
    @Autowired
    DataHelperService dataHelperService;

    /**
     * 保存单个实体类到一个json文件，不含日期
     */
    @Test
    public void test1() {
        PersonBean person = new PersonBean();
        person.setAge(10);
        person.setName("haha");

        dataHelperService.store(PersonBean.class.getSimpleName(),person);
        //以下日期传入null，实现同样的效果
        //dataHelperService.store(PersonBean.class.getSimpleName(),null,person);
    }

    /**
     * 使用list对象，一次保存多个实体类到一个json文件
     */
    @Test
    public void test2() {
        PersonBean p1 = new PersonBean();
        p1.setName("p1");
        p1.setAge(10);
        PersonBean p2 = new PersonBean();
        p2.setAge(20);
        p2.setName("p2");

        List<PersonBean> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);

        dataHelperService.store(PersonBean.class.getSimpleName(),list);
    }

    /**
     * 按照日期区分
     */
    @Test
    public void t3() {
        PersonBean person = new PersonBean();
        person.setAge(10);
        person.setName("haha");

        dataHelperService.store(PersonBean.class.getSimpleName(),new Date(),person);
    }

    @Test
    public void t4() {
        PersonBean person = new PersonBean();
        person.setAge(10);
        person.setName("haha");

    }
}
