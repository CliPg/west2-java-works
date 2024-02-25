package com.clipg.mapper;

import com.clipg.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapper {
    //查
    //查看所有
    List<Order> selectAll();
    //查看详情
    Order selectByid(int id);
    //动态条件查询
    List<Order> selectByDynamicCondition(Map map);
    //时间降序
    List<Order> selectAllDateDESC();
    //时间升序
    List<Order> selectAllDateASC();
    //id降序
    List<Order> selectAllIdDESC();

    //改
    //动态修改
    void updateDynamic(Order order);

    //增
    void insertOrder(Order orders);

    //删
    //删除指定
    void deleteById(int id);
    //批量删除
    void deleteByIds(@Param("orderIds") int [] ids);
}
