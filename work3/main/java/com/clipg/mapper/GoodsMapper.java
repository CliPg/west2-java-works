package com.clipg.mapper;

import com.clipg.pojo.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GoodsMapper {
    //查
    //查看所有
    List<Goods> selectAll();
    //查看详情
    Goods selectByName(String goodsName);
    Goods selectById(int goodsId);
    //动态条件查询
    List<Goods> selectByDynamicCondition(Map map);
    //价格升序
    List<Goods> selectAllPriceASC();
    //价格降序
    List<Goods> selectAllPriceDESC();
    //id降序
    List<Goods> selectAllIdDESC();

    //改
    //动态修改
    void updateDynamic(Goods goods);

    //增
    void insertGood(Goods goods);

    //删
    //删除指定
    void deleteById(int id);
    //批量删除
    void deleteByIds(@Param("goodsIds") int[] ids);
}
