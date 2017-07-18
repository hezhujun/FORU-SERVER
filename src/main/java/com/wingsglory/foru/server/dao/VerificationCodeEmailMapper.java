package com.wingsglory.foru.server.dao;

import com.wingsglory.foru.server.model.VerificationCodeEmail;
import com.wingsglory.foru.server.model.VerificationCodeEmailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface VerificationCodeEmailMapper {
    int countByExample(VerificationCodeEmailExample example);

    int deleteByExample(VerificationCodeEmailExample example);

    int deleteByPrimaryKey(String phone);

    int insert(VerificationCodeEmail record);

    int insertSelective(VerificationCodeEmail record);

    List<VerificationCodeEmail> selectByExample(VerificationCodeEmailExample example);

    VerificationCodeEmail selectByPrimaryKey(String phone);

    int updateByExampleSelective(@Param("record") VerificationCodeEmail record, @Param("example") VerificationCodeEmailExample example);

    int updateByExample(@Param("record") VerificationCodeEmail record, @Param("example") VerificationCodeEmailExample example);

    int updateByPrimaryKeySelective(VerificationCodeEmail record);

    int updateByPrimaryKey(VerificationCodeEmail record);
}