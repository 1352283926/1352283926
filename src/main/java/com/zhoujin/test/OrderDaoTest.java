package com.zhoujin.test;

import com.zhoujin.dao.OrderDao;
import com.zhoujin.dao.impl.OrderDaoImpl;
import com.zhoujin.pojo.Order;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;

public class OrderDaoTest {

    @Test
    public void saveOrder() throws Exception {

        OrderDao orderDao = new OrderDaoImpl();

        orderDao.saveOrder(new Order("1234567891",new Date(),new BigDecimal(100),0, 1));

    }
}