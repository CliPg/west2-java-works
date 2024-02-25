package com.clipg;

import com.clipg.service.GoodsService;
import com.clipg.service.OrderService;

import java.io.IOException;
import java.text.ParseException;

public class OrderManagementTest {
    public static void main(String[] args) throws IOException, ParseException {
        //GoodsCURD
        GoodsService goodsService = new GoodsService();
        //正常范例 增
        goodsService.insertGoods("ZK6",4000,50,9);
        //Id已存在
        goodsService.insertGoods("ZK6",4000,50,8);
        //num非正
        goodsService.insertGoods("ZK6",4000,-1,10);
        //price非正
        goodsService.insertGoods("ZK6",-4000,1,10);
        //name为空
        goodsService.insertGoods("",4000,5,11);

        //正常范例 删
        goodsService.deleteGoodsById(10);
        //批量删除
        //int [] goodsIds = {1,2};
        //goodsService.goodsDeleteInBatches(goodsIds);
        //id不存在
        goodsService.deleteGoodsById(11);
        goodsService.deleteGoodsById(0);
        goodsService.deleteGoodsById(-1);
        //删除的商品已存在在订单中，会取消相应订单
        goodsService.deleteGoodsById(2);

        //正常范例 改
        goodsService.updateGoodsById("ZK6", 5000, 5, 9);
        //id不存在
        goodsService.updateGoodsById("ZK6",5000,5,10);
        //price非正
        goodsService.updateGoodsById("ZK6",5000,5,10);
        //name为空
        goodsService.updateGoodsById("",5000,5,9);

        //正常范例 查
        goodsService.getGoodsById(1);
        goodsService.getGoodsByName("PG1");
        //id不存在
        goodsService.getGoodsById(-1);


        //OrderCURD
        OrderService orderService = new OrderService();
        //增
        orderService.insertOrder(8,"ZK4",2);
        //删
        orderService.deleteOrderById(5);
        //批量删除
        //int [] orderIds = {1,2}
        //orderService.orderDeleteInBatches(orderIds);
        //改
        orderService.orderUpdate(4, "PG3", 5, "2023-10-16 13:50:56");
        //查
        orderService.getOrderById(4);


    }
}
