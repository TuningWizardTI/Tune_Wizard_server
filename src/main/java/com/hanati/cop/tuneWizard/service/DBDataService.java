package com.hanati.cop.tuneWizard.service;

import com.hanati.cop.tuneWizard.dao.ChatTableListDAO;
import com.hanati.cop.tuneWizard.dao.MakePromptTableInfoDAO;
import com.hanati.cop.tuneWizard.dto.TableInfoListRequestDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface DBDataService {
    List<String> tableList();

    List<String> tableIndexList();

    List<String> indexList();

    HashMap<String, ArrayList<String>> makePromptTableInfo(TableInfoListRequestDTO tableInfoListRequestDTO);
}
