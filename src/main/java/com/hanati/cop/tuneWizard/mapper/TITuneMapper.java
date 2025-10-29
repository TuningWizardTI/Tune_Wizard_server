package com.hanati.cop.tuneWizard.mapper;

import com.hanati.cop.tuneWizard.dao.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TITuneMapper {
    List<ChatTableListDAO> tableList();
    List<ChatTableIndexListDAO> tableIndexList(String tableName);

    List<MakePromptTableInfoDAO> tableInfoList(String tableName);

    TableCountDAO tableCount(String tableName);

    int insertPrompt(PromptInsertDAO promptInsertDAO);

    List<GetPromptHistoryDAO> listHistory(String userId);

    int insertUserInfo(LoginInfoTableDAO loginInfoTableDAO);

    int updateUserInfo(String status, String userId);

    List<LoginInfoTableDAO> inquiryUserInfo(String userId);

}
