package com.hanati.cop.tuneWizard.service;

import com.hanati.cop.tuneWizard.config.ChatGPTConfig;
import com.hanati.cop.tuneWizard.dao.ChatTableListDAO;
import com.hanati.cop.tuneWizard.mapper.TITuneMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<String> columnList() {
        return null;
    }

    public List<String> indexList() {
        return null;
    }
}
