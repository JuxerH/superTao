package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.ShoppingCartDO;

import java.util.List;

public interface ShoppingCartDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated Mon Feb 03 15:40:47 CST 2020
     */
    int deleteByPrimaryKey(Integer orderId);
    int deleteByUserId(Integer userId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated Mon Feb 03 15:40:47 CST 2020
     */
    int insert(ShoppingCartDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated Mon Feb 03 15:40:47 CST 2020
     */
    int insertSelective(ShoppingCartDO record);
    List<ShoppingCartDO> listCartDO(Integer userId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated Mon Feb 03 15:40:47 CST 2020
     */
    ShoppingCartDO selectByPrimaryKey(Integer orderId);
    ShoppingCartDO selectByItemId(Integer itermId,Integer userId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated Mon Feb 03 15:40:47 CST 2020
     */
    int updateByPrimaryKeySelective(ShoppingCartDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shopping_cart
     *
     * @mbg.generated Mon Feb 03 15:40:47 CST 2020
     */
    int updateByPrimaryKey(ShoppingCartDO record);
    int updateByItemId(ShoppingCartDO record);
}