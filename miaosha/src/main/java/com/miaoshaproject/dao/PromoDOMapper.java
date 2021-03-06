package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.PromoDO;
import org.springframework.stereotype.Component;

@Component
public interface PromoDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Mon Nov 11 19:04:37 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Mon Nov 11 19:04:37 CST 2019
     */
    int insert(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Mon Nov 11 19:04:37 CST 2019
     */
    int insertSelective(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Mon Nov 11 19:04:37 CST 2019
     */
    PromoDO selectByPrimaryKey(Integer id);

    PromoDO selectByItemId(Integer itemId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Mon Nov 11 19:04:37 CST 2019
     */
    int updateByPrimaryKeySelective(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Mon Nov 11 19:04:37 CST 2019
     */
    int updateByPrimaryKey(PromoDO record);
}