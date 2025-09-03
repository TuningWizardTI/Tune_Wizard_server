package com.hanati.cop.tuneWizard.mapper;

import com.hanati.cop.tuneWizard.dao.ChatTableIndexListDAO;
import com.hanati.cop.tuneWizard.dao.ChatTableListDAO;
import com.hanati.cop.tuneWizard.dao.MakePromptTableInfoDAO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TITuneMapper {
    List<ChatTableListDAO> tableList();
    List<ChatTableIndexListDAO> tableIndexList(String tableName);

    List<MakePromptTableInfoDAO> tableInfoList(String tableName);

}
