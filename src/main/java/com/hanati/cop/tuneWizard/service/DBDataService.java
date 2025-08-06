package com.hanati.cop.tuneWizard.service;

import com.hanati.cop.tuneWizard.dao.ChatTableListDAO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public interface DBDataService {
    List<String> tableList();

    List<String> columnList();

    List<String> indexList();
}
