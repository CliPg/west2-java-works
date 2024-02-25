package com.clipg.pojo;

import java.util.Date;

/**
 * Order类表示数据库中商品信息
 * 该类包含用户的基本信息和操作
 * @author 77507
 */
public class Order {
    /**
     * 订单的唯一标识
     */
    private int orderId;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 订单所购买的商品数量
     */
    private int orderNum;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 订单总金额
     */
    private int orderPrice;

    /**
     * 获取订单id
     * @return 订单id
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * 设置订单id
     * @param orderId 订单id
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取商品名
     * @return 商品名
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * 设置商品名
     * @param goodsName 商品名
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /**
     * 获取订单所购买的商品数量
     * @return 订单所购买的商品数量
     */
    public int getOrderNum() {
        return orderNum;
    }

    /**
     * 设置订单所购买的商品数量
     * @param orderNum 订单所购买的商品数量
     */
    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 获取下单时间
     * @return 下单时间
     */
    public Date getOrderTime() {
        return orderTime;
    }

    /**
     * 设置下单时间
     * @param orderTime 下单时间
     */
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * 获取订单总金额
     * @return 订单总金额
     */
    public int getOrderPrice() {
        return orderPrice;
    }

    /**
     * 设置订单总金额
     * @param orderPrice 订单总金额
     */
    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    /**
     * 打印订单信息
     * @return 订单信息
     */
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", goodName='" + goodsName + '\'' +
                ", orderNum=" + orderNum +
                ", orderTime=" + orderTime +
                ", orderPrice=" + orderPrice +
                '}';
    }
}
