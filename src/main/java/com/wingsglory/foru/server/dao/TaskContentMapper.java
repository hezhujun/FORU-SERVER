package com.wingsglory.foru.server.dao;

import com.wingsglory.foru.server.model.TaskContent;
import com.wingsglory.foru.server.model.TaskContentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskContentMapper {
    int countByExample(TaskContentExample example);

    int deleteByExample(TaskContentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TaskContent record);

    int insertSelective(TaskContent record);

    List<TaskContent> selectByExample(TaskContentExample example);

    TaskContent selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TaskContent record, @Param("example") TaskContentExample example);

    int updateByExample(@Param("record") TaskContent record, @Param("example") TaskContentExample example);

    int updateByPrimaryKeySelective(TaskContent record);

    int updateByPrimaryKey(TaskContent record);
}