package com.hanati.cop.tuneWizard.mapper;

import com.hanati.cop.tuneWizard.dao.ChatTableListDAO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TITuneMapper {
    List<ChatTableListDAO> tableList();

    //List<ChatTableListDAO> ColumnList();
}
