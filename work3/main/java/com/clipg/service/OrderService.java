package com.clipg.service;

import com.clipg.exception.GoodsNameNotFoundException;
import com.clipg.exception.OrderNumFalseException;
import com.clipg.exception.OrderPriceFalseException;
import com.clipg.mapper.GoodsMapper;
import com.clipg.mapper.OrderMapper;
import com.clipg.pojo.Goods;
import com.clipg.pojo.Order;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * OrderService类表示关于商品的各类操作，
 * 该类包含对商品进行curd等各种方法
 * @author 77507
 */

public class OrderService {

    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

    SqlSession sqlSession = sqlSessionFactory.openSession();

    GoodsMapper goodsMapper = sqlSession.getMapper(GoodsMapper.class);
    OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

    public OrderService() throws IOException {
    }

    /**
     * 增加订单
     * @param orderId 订单id
     * @param goodsName 商品名
     * @param orderNum 订单所购买商品数量
     */
    public void insertOrder(int orderId, String goodsName, int orderNum) throws IOException {
        //将下单时间设置为当前时间
        Date orderTime = new Date();
        Goods goods = goodsMapper.selectByName(goodsName);
        int orderPrice = orderNum * goods.getGoodsPrice();
        Order order = new Order();
        order.setOrderId(orderId);
        order.setGoodsName(goodsName);
        order.setOrderNum(orderNum);
        order.setOrderTime(orderTime);
        order.setOrderPrice(orderPrice);
        //检查订单
        if (orderCheck(order)) {
            orderMapper.insertOrder(order);
        } else {
            System.out.println("请重新确认订单");
        }
        //更新货存
        goods.setGoodsNum(goods.getGoodsNum() - orderNum);
        goodsMapper.updateDynamic(goods);
        sqlSession.commit();
        //sqlSession.close();
    }

    /**
     * 删除订单
     * @param orderId 订单id
     */
    public void deleteOrderById(int orderId) {
        //检查订单信息
        boolean flag = true;
        if (orderId <= 0 || orderId > orderIdMax()) {
            flag = false;
            System.out.println("订单不存在");
        }
        //若检查正确，进行删除
        while (flag) {
            orderMapper.deleteById(orderId);
            Order order = orderMapper.selectByid(orderId);
            Goods goods = goodsMapper.selectByName(order.getGoodsName());
            //相应的库存增加
            goods.setGoodsNum(goods.getGoodsNum() + order.getOrderNum());
            goodsMapper.updateDynamic(goods);
            sqlSession.commit();
            //sqlSession.close();
        }
    }

    /**
     * 批量删除
     * @param orderIds 订单id
     */
    public void orderDeleteInBatches(int[] orderIds) {
        orderMapper.deleteByIds(orderIds);
        sqlSession.commit();
        //sqlSession.close();
    }

    /**
     * 修改订单
     * @param orderId 订单id
     * @param goodsName 商品名
     * @param orderNum 订单所购买的商品数量
     * @param orderTime 下单时间
     */
    public void orderUpdate(int orderId, String goodsName, int orderNum, String orderTime) throws ParseException {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date ordertime = dateformat.parse(orderTime);
        Goods goods = goodsMapper.selectByName(goodsName);
        int orderPrice = goods.getGoodsPrice() * orderNum;
        Order order = new Order();
        order.setOrderId(orderId);
        order.setGoodsName(goodsName);
        order.setOrderNum(orderNum);
        order.setOrderTime(ordertime);
        order.setOrderPrice(orderPrice);
        orderMapper.updateDynamic(order);
        sqlSession.commit();
        //sqlSession.close();
    }

    /**
     * 查找订单（通过id）
     * @param orderId 订单id
     */
    public void getOrderById(int orderId) {
        //检查订单信息
        boolean flag = true;
        if (orderId <= 0 || orderId > orderIdMax()) {
            flag = false;
            System.out.println("订单不存在");
        }
        //若检查正确，则查找订单
        if (flag) {
            Order order = orderMapper.selectByid(orderId);
            System.out.println(order);
            sqlSession.close();
        }
    }

    /**
     * 获取所有订单
     */
    public void getOrderAll() {
        List<Order> order = orderMapper.selectAll();
        System.out.println(order);
        //sqlSession.close();
    }

    /**
     * 按时间降序查询
     */
    public void getOrderDateDESC() {
        List<Order> order = orderMapper.selectAllDateDESC();
        System.out.println(order);
        //sqlSession.close();
    }

    /**
     * 按时间升序查询
     */
    public void orderSelectDateASC() {
        List<Order> order = orderMapper.selectAllDateDESC();
        System.out.println(order);
        //sqlSession.close();
    }

    /**
     * 检查订单
     */
    public boolean orderCheck(Order order) throws IOException {
        //检查商品是否存在
        boolean flag1 = true;
        try {
            boolean flag2 = false;
            for (int i = 1; i <= goodsIdMax(); i++) {
                Goods goods = goodsMapper.selectById(i);
                if (order.getGoodsName().equals(goods.getGoodsName())) {
                    flag2 = true;
                    break;
                }
            }
            if (flag2 == false) {
                flag1 = false;
                throw new GoodsNameNotFoundException("商品名不存在");
            }
        } catch (GoodsNameNotFoundException i) {
            System.out.println(i.toString());
        }
        //检查订单价格
        try {
            Goods goods = goodsMapper.selectByName(order.getGoodsName());
            if (goods.getGoodsPrice() != order.getOrderPrice() / order.getOrderNum()) {
                flag1 = false;
                throw new OrderPriceFalseException("订单价格错误");
            }
        } catch (OrderPriceFalseException i) {
            System.out.println(i.toString());
        }
        //检查购买数量
        try {
            Goods goods = goodsMapper.selectByName(order.getGoodsName());
            if (goods.getGoodsNum() < order.getOrderNum()) {
                flag1 = false;
                throw new OrderNumFalseException("购买数量超过货存");
            }
        } catch (OrderNumFalseException i) {
            System.out.println(i.toString());
        }
        //检查id
        try {

            for (int i = 1; i <= goodsIdMax(); i++) {
                Goods goods = goodsMapper.selectById(i);
                if (order.getOrderId() == goods.getGoodsId()) {
                    flag1 = true;
                    throw new GoodsNameNotFoundException("商品id已存在");
                }
            }
        } catch (GoodsNameNotFoundException i) {
            System.out.println(i.toString());
        }

        return flag1;
    }

    /**
     * 求总商品数量
     *
     * @return 总商品数量的值
     */
    public int goodsIdMax() {
        List<Goods> goods = goodsMapper.selectAllIdDESC();
        return goods.get(0).getGoodsId();
    }

    /**
     * 求总订单数量
     *
     * @return 总订单数量的值
     */
    public int orderIdMax() {
        List<Order> orders = orderMapper.selectAllIdDESC();
        return orders.get(0).getOrderId();
    }


}
