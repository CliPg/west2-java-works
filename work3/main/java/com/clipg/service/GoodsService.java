package com.clipg.service;

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
import java.util.List;

/**
 * GoodsService类表示关于商品的各类操作，
 * 该类包含对商品进行curd等各种方法
 * @author 77507
 */

public class GoodsService {
    /**
     * 1.获取sqlSessionFactory
     */
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

    /**
     * 2.获取sqlSession对象
     */
    SqlSession sqlSession = sqlSessionFactory.openSession();

    /**
     * 3.获取Mapper接口的代理对象
     */
    GoodsMapper goodsMapper = sqlSession.getMapper(GoodsMapper.class);
    OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

    public GoodsService() throws IOException {
    }


    /**
     * 增加商品
     *
     * @param goodsName 商品名
     * @param goodsPrice 商品价格
     * @param goodsNum 商品库存
     * @param goodsId 商品id
     */
    public void insertGoods(String goodsName, int goodsPrice, int goodsNum, int goodsId) {
        //检查新建商品信息是否正确
        boolean flag = true;
        //检查商品是否存在
        for (int i = 1; i <= goodsIdMax(); i++) {
            Goods goods = goodsMapper.selectById(i);
            if (goods.getGoodsId() == goodsId) {
                flag = false;
                System.out.println(i + "号商品已存在");
            }
        }
        //检查商品数量是否为正
        if (goodsNum <= 0) {
            flag = false;
            System.out.println("商品数量需为正数");
        }
        //检查商品价格是否为正
        if (goodsPrice <= 0) {
            flag = false;
            System.out.println("商品价格需为正数");
        }
        //检查商品名是否为空
        if (goodsName.equals("") || goodsName == null) {
            flag = false;
            System.out.println("商品名不能为空");
        }
        //若检查正确，则新建商品
        if (flag) {
            Goods goods = new Goods();
            goods.setGoodsName(goodsName);
            goods.setGoodsPrice(goodsPrice);
            goods.setGoodsNum(goodsNum);
            goods.setGoodsId(goodsId);
            goodsMapper.insertGood(goods);
            sqlSession.commit();
            //sqlSession.close();
        }
    }

    /**
     * 删除商品（按照id）
     *
     * @param goodsId
     */
    public void deleteGoodsById(int goodsId) {
        //检查商品是否存在
        if (goodsId > goodsIdMax() || goodsId <= 0) {
            System.out.println("商品不存在");
        }
        Goods goods = goodsMapper.selectById(goodsId);
        //若商品已存在与订单，则取消该订单
        for (int j = 1; j <= orderIdMax(); j++) {
            Order order = orderMapper.selectByid(j);
            if (goods.getGoodsName().equals(order.getGoodsName())) {
                orderMapper.deleteById(j);
                System.out.println(j + "号订单取消");
            }
        }
        //删除订单
        goodsMapper.deleteById(goodsId);
        sqlSession.commit();
        //sqlSession.close();
    }

    /**
     * 批量删除
     * 判断订单内是否存在该商品，若存在，则取消该订单
     *
     * @param goodsIds 商品id
     */
    public void deleteGoodsInBatches(int[] goodsIds) {
        //若商品已存在与订单，则取消该订单
        for (int i = 0; i < goodsIds.length; i++) {
            Goods goods2 = goodsMapper.selectById(goodsIds[i]);
            for (int j = 1; j <= orderIdMax(); j++) {
                Order order = orderMapper.selectByid(j);
                if (goods2.getGoodsName().equals(order.getGoodsName())) {
                    orderMapper.deleteById(j);
                    System.out.println(j + "号订单取消");
                }
            }
        }
        //删除商品
        goodsMapper.deleteByIds(goodsIds);
        sqlSession.commit();
        //sqlSession.close();
    }

    /**
     * 修改商品（通过id）
     *
     * @param goodsName 商品名
     * @param goodsPrice 商品价格
     * @param goodsNum 商品库存
     * @param goodsId 商品id
     */
    public void updateGoodsById(String goodsName, int goodsPrice, int goodsNum, int goodsId) {
        //检查商品信息
        boolean flag = true;
        //检查商品是否存在
        if (goodsId > goodsIdMax() || goodsId <= 0) {
            flag = false;
            System.out.println("商品不存在");
        }
        //检查商品名是否为空
        if (goodsName.equals("") || goodsName == null) {
            flag = false;
            System.out.println("商品名不能为空");
        }
        //若检查正确，则修改商品信息
        if (flag) {
            Goods goods = new Goods();
            goods.setGoodsName(goodsName);
            goods.setGoodsPrice(goodsPrice);
            goods.setGoodsNum(goodsNum);
            goods.setGoodsId(goodsId);
            goodsMapper.updateDynamic(goods);
            sqlSession.commit();
            //sqlSession.close();
        }
    }

    /**
     * 查询商品（通过id）
     *
     * @param goodsId 商品id
     */
    public void getGoodsById(int goodsId) {
        //检查产品信息
        boolean flag = true;
        //检查商品是否存在
        if (goodsId > goodsIdMax() || goodsId <= 0) {
            flag = false;
            System.out.println("商品不存在");
        }
        //若检查正确，则获取商品信息
        if (flag) {
            Goods goods = goodsMapper.selectById(goodsId);
            System.out.println(goods);
            //sqlSession.close();
        }
    }

    /**
     * 查询商品（通过名字）
     *
     * @param goodsName 商品名
     */
    public void getGoodsByName(String goodsName) {
        Goods goods = goodsMapper.selectByName(goodsName);
        System.out.println(goods);
        //sqlSession.close();
    }

    /**
     * 按照价格升序查询
     */
    public void getGoodsPriceASC() {
        List<Goods> goods = goodsMapper.selectAllPriceASC();
        System.out.println(goods);
        //sqlSession.close();
    }

    /**
     * 按照价格降序查询
     */
    public void getGoodsPriceDESC() {
        List<Goods> goods = goodsMapper.selectAllPriceASC();
        System.out.println(goods);
        //sqlSession.close();
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
