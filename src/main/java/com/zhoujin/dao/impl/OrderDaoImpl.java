package com.zhoujin.dao.impl;

import com.zhoujin.dao.OrderDao;
import com.zhoujin.pojo.Order;

import java.util.List;

public class OrderDaoImpl extends BaseDao implements OrderDao {
    @Override
    public void saveOrder(Order order) {
        // sql语句
        String sql = "insert into t_order(`order_id`,`create_time`,`total_money`,`status`,`user_id`) values(?,?,?,?,?)";

        // 执行sql语句
        update(sql, order.getOrderId(), order.getCreateTime(), order.getPrice(), order.getStatus(), order.getUserId());
    }

    @Override
    public List<Order> queryMyOrders(int userId) throws Exception {
        // 查询我的订单
        String sql = "select `order_id` orderId,`create_time` createTime,`total_money` totalMoney,`status`,`user_id` userId from t_order where `user_id` = ?";
        // 执行sql语句
        return queryForList(Order.class,sql, userId);
    }


    @Override
    public List<Order> queryAllOrders() throws Exception {
        // 查询我的订单
        String sql = "select `order_id` orderId,`create_time` createTime,`total_money` totalMoney,`status`,`user_id` userId from t_order ";
        // 执行sql语句
        return queryForList(Order.class,sql);
    }

    @Override
    public void updateOrderStatus(int status, String orderId) throws Exception {
        // sql语句
        String sql = "update t_order set status = ? where order_id = ?";
        // 执行sql语句
        update(sql, status, orderId);
    }
}
