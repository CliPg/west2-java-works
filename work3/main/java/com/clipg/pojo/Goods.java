package com.clipg.pojo;

/**
 * Goods类表示数据库中商品信息
 * 该类包含用户的基本信息和操作
 * @author 77507
 */
public class Goods {
    /**
     * 商品的唯一标识
     */
    private int goodsId;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 商品价格
     */
    private int goodsPrice;

    /**
     * 商品库存
     */
    private int goodsNum;

    /**
     * 获取商品id
     *
     * @return 商品id
     */
    public int getGoodsId() {
        return goodsId;
    }

    /**
     * 设置商品id
     * @param goodsId 商品id
     */
    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取商品名
     *
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
     * 获取商品价格
     * @return 商品价格
     */
    public int getGoodsPrice() {
        return goodsPrice;
    }

    /**
     * 设置商品价格
     * @param goodsPrice 商品价格
     */
    public void setGoodsPrice(int goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    /**
     * 获取商品库存
     * @return 商品库存数量
     */
    public int getGoodsNum() {
        return goodsNum;
    }

    /**
     * 设置商品库存
     * @param goodsNum 商品库存
     */
    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    /**
     * 打印商品信息
     * @return 商品信息
     */
    @Override
    public String toString() {
        return "Goods{" +
                "goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsNum=" + goodsNum +
                '}';
    }
}
