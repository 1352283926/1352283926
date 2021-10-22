package com.zhoujin.test;

import com.zhoujin.utils.JdbcUtils;
import org.junit.Test;

import java.sql.Connection;

public class JdbcUtilsTest {

    @Test
    public void testJdbcUtils(){
//        for (int i = 0; i < 100; i++){
            Connection connection = JdbcUtils.getConnection();
            System.out.println(connection);
            com.alibaba.druid.util.JdbcUtils.close(connection);
        }
}

