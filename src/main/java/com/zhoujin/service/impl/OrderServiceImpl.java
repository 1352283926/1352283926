package com.zhoujin.service.impl;

import com.zhoujin.dao.BookDao;
import com.zhoujin.dao.OrderDao;
import com.zhoujin.dao.OrderItemDao;
import com.zhoujin.dao.impl.BookDaoImpl;
import com.zhoujin.dao.impl.OrderDaoImpl;
import com.zhoujin.dao.impl.OrderItemDaoImpl;
import com.zhoujin.pojo.*;
import com.zhoujin.service.OrderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao = new OrderDaoImpl();
    private OrderItemDao orderItemDao = new OrderItemDaoImpl();
    private BookDao bookDao = new BookDaoImpl();

    public OrderServiceImpl() {
        super();

        orderDao = new OrderDaoImpl();
        orderItemDao = new OrderItemDaoImpl();
        bookDao = new BookDaoImpl();
    }

    @Override
    public String createOrder(Cart cart, Integer userId) throws Exception {

        System.out.println(" OrderServiceImpl 程序在[" +Thread.currentThread().getName() + "]中");

        // 订单号===唯一性
        String orderId = System.currentTimeMillis()+""+userId;
        // 创建一个订单对象
        Order order = new Order(orderId,new Date(),cart.getTotalPrice(), 0,userId);
        // 保存订单
        orderDao.saveOrder(order);
        orderDao.saveOrder(order);
        //注释的
        //int i = 12 / 0;

        // 遍历购物车中每一个商品项转换成为订单项保存到数据库
        for (Map.Entry<Integer, CartItem>entry : cart.getItems().entrySet()){
            // 获取每一个购物车中的商品项
            CartItem cartItem = entry.getValue();
            // 转换为每一个订单项
            OrderItem orderItem = new OrderItem(null,cartItem.getName(),cartItem.getCount(),cartItem.getPrice(),cartItem.getTotalPrice(), orderId);
            // 保存订单项到数据库
            orderItemDao.saveOrderItem(orderItem);

            // 更新库存和销量
            Book book = bookDao.queryBookById(cartItem.getId());
            book.setSales( book.getSales() + cartItem.getCount() );
            book.setStock( book.getStock() - cartItem.getCount() );
            bookDao.updateBook(book);

        }
        // 清空购物车
        cart.clear();

        return orderId;
    }

    @Override
    public String saveOrder(Cart cart, int userId) throws Exception {
        // 创建订单对象
        Order order = new Order();
        order.setCreateTime(new Date());
        order.setPrice(cart.getTotalPrice());
        order.setUserId(userId);
        order.setStatus(0);
        // 生成订单号
        String orderId = System.currentTimeMillis() + "" + userId;
        order.setOrderId(orderId);
        // 保存订单
        orderDao.saveOrder(order);

        // 遍历购物车中有每一个商品
        List<OrderItem> items = new ArrayList<OrderItem>();
        for (CartItem cartItem : cart.getItems().values()) {
            // 生成订单项
            OrderItem orderItem = new OrderItem(0, cartItem.getName(), cartItem.getCount(),
                    cartItem.getPrice(), cartItem.getTotalPrice(), orderId);
//			// 保存订单项
//			orderitemDao.saveOrderItem(orderItem);
            items.add(orderItem);
            // 修改图书的库存和销量
            Book book = bookDao.queryBookById(cartItem.getId());
            book.setStock( book.getStock() - cartItem.getCount() );
            book.setSales( book.getSales() + cartItem.getCount() );
            bookDao.updateBook(book);
        }
        // 批量插入
        orderItemDao.batchSaveOrderItem(items);
        // 清空购物车
        cart.clear();

        return orderId;
    }

    @Override
    public List<Order> queryMyOrders(int userId) throws Exception {
        // 查找自己的订单
        return orderDao.queryMyOrders(userId);
    }

    @Override
    public List<OrderItem> queryOrderItems(String orderId) throws Exception {
        // 查找某个订单的订单项
        return orderItemDao.queryOrderItems(orderId);
    }

    @Override
    public List<Order> queryAllOrders() throws Exception {
        // 查询所有订单
        return orderDao.queryAllOrders();
    }

    @Override
    public void sendOrder(String orderId) throws Exception {
        // 修改订单状态为已发货
        orderDao.updateOrderStatus(1, orderId);
    }

    @Override
    public void receivedOrder(String orderId) throws Exception {
        // 修改订单状态为已发货
        orderDao.updateOrderStatus(2, orderId);
    }
}
