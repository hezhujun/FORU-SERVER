package com.wingsglory.foru.server.dao;

import com.wingsglory.foru.server.model.Addressee;
import com.wingsglory.foru.server.model.AddresseeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AddresseeMapper {
    int countByExample(AddresseeExample example);

    int deleteByExample(AddresseeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Addressee record);

    int insertSelective(Addressee record);

    List<Addressee> selectByExample(AddresseeExample example);

    Addressee selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Addressee record, @Param("example") AddresseeExample example);

    int updateByExample(@Param("record") Addressee record, @Param("example") AddresseeExample example);

    int updateByPrimaryKeySelective(Addressee record);

    int updateByPrimaryKey(Addressee record);
}