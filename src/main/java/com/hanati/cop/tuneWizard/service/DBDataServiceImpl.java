package com.hanati.cop.tuneWizard.service;

import com.hanati.cop.tuneWizard.config.ChatGPTConfig;
import com.hanati.cop.tuneWizard.dao.ChatTableListDAO;
import com.hanati.cop.tuneWizard.dao.MakePromptTableInfoDAO;
import com.hanati.cop.tuneWizard.dto.TableInfoListRequestDTO;
import com.hanati.cop.tuneWizard.mapper.TITuneMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DBDataServiceImpl implements DBDataService{

    public DBDataServiceImpl() {

    }

    @Autowired
    public TITuneMapper mapperClass;


    public List<String> tableList() {
        List<ChatTableListDAO> list = mapperClass.tableList();
        List<String> result = new ArrayList<String>();
        for(int i = 0 ;i<list.size(); i++) {
            ChatTableListDAO dao = list.get(i);
            String table_name = dao.getTable_name();
            result.add(table_name);
        }

        return result;

    }

    public List<String> tableIndexList() {
        return null;
    }

    public List<String> indexList() {
        return null;
    }

    @Override
    public HashMap<String, ArrayList<String>> makePromptTableInfo(TableInfoListRequestDTO tableInfoListRequestDTO) {
        System.out.println("tableName" + tableInfoListRequestDTO.getTableName());
        HashMap<String, ArrayList<String>> result = new HashMap<>();
        List<MakePromptTableInfoDAO> tableinfolist = mapperClass.tableInfoList(tableInfoListRequestDTO.getTableName());
        ArrayList<String> columeList = new ArrayList<String>();
        ArrayList<String> columeType = new ArrayList<String>();

        for(int i = 0; i<tableinfolist.size(); i ++) {
            columeList.add(tableinfolist.get(i).getColumn_name());
            columeType.add(tableinfolist.get(i).getColume_type());
            System.out.println("ColumnName = " + tableinfolist.get(i).getColumn_name() + "\nColumnType = " + tableinfolist.get(i).getColume_type());
        }

        result.put("Column",columeList);
        result.put("Type",columeType);

        return result;
    }
}
