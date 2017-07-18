package com.wingsglory.foru.server.dao;

import com.wingsglory.foru.server.model.VerificationCodePhone;
import com.wingsglory.foru.server.model.VerificationCodePhoneExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VerificationCodePhoneMapper {
    int countByExample(VerificationCodePhoneExample example);

    int deleteByExample(VerificationCodePhoneExample example);

    int deleteByPrimaryKey(String phone);

    int insert(VerificationCodePhone record);

    int insertSelective(VerificationCodePhone record);

    List<VerificationCodePhone> selectByExample(VerificationCodePhoneExample example);

    VerificationCodePhone selectByPrimaryKey(String phone);

    int updateByExampleSelective(@Param("record") VerificationCodePhone record, @Param("example") VerificationCodePhoneExample example);

    int updateByExample(@Param("record") VerificationCodePhone record, @Param("example") VerificationCodePhoneExample example);

    int updateByPrimaryKeySelective(VerificationCodePhone record);

    int updateByPrimaryKey(VerificationCodePhone record);
}