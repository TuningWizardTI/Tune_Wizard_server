package com.hanati.cop.tuneWizard.service;

import com.hanati.cop.tuneWizard.dao.*;
import com.hanati.cop.tuneWizard.dto.*;
import com.hanati.cop.tuneWizard.mapper.TITuneMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

    @Override
    public List<GetPromptHistoryDAO> getPromptHistory(GetPromptHistoryDTO getPromptHistoryDTO) {
        HashMap<String, ArrayList<String>> result = new HashMap<>();
        List<GetPromptHistoryDAO> historyList = mapperClass.listHistory(getPromptHistoryDTO.getUser_id());

        //일단 요정도만 불러내보자

        return historyList;
    }
    @Override
    public HashMap<String, LoginResponseDTO> signUpUser(SignUpUserInfoRequestDTO signUpUserInfoRequestDTO) {
        HashMap<String, LoginResponseDTO> result = new HashMap<String, LoginResponseDTO>();
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        //3. 최종 요청 프롬포트와 응답 프롬포트 데이터를 신규 테이블에 입력
        LoginInfoTableDAO loginInfoTableDAO = new LoginInfoTableDAO();
        loginInfoTableDAO.setUserId(signUpUserInfoRequestDTO.getUserId());
        loginInfoTableDAO.setPassword(signUpUserInfoRequestDTO.getPassword());
        loginInfoTableDAO.setUserName(signUpUserInfoRequestDTO.getUserName());
        loginInfoTableDAO.setLoginStatus(signUpUserInfoRequestDTO.getLoginStatus());
        System.out.println("getUserId = " + signUpUserInfoRequestDTO.getUserId()
                + "\ngetPassword = " + signUpUserInfoRequestDTO.getPassword()
                + "\ngetUserName = " + signUpUserInfoRequestDTO.getUserName()
                + "\ngetLoginStatus = " + signUpUserInfoRequestDTO.getLoginStatus());

        try{
            int promptinsertResult = 0;
            promptinsertResult = mapperClass.insertUserInfo(loginInfoTableDAO);
            if(promptinsertResult == 0){
                throw new SQLException("정상적으로 삽입되지 않았습니다.");

            }else {
                //응답코드 세팅으로 고려해본다.
                loginResponseDTO.setResponse("200");
            }
        }catch (SQLException e ){
            e.getStackTrace();
        }
        return result;
    }

    @Override
    public HashMap<String, LoginResponseDTO> loginUser(LoginRequestDTO loginRequestDTO) {

        HashMap<String, LoginResponseDTO> result = new HashMap<String, LoginResponseDTO>();
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        List<LoginInfoTableDAO> loginInfoTableDAO = null;
        int loginResult = 0;
        //로그인 정보 select
        LoginInfoTableDAO loginInfoTableDAOParam = new LoginInfoTableDAO();
        //loginInfoTableDAOParam.setPassword(loginRequestDTO.getPassword());

        String userId = loginRequestDTO.getUserId();
        String password = loginRequestDTO.getPassword();

        System.out.println("userid3 : " + userId +  "password" + password);
        try {
            if("".equals(userId)) {
                throw new SQLException("필수값 입력 오류입니다 : USER_ID");
            }

            if("".equals(password)) {
                throw new SQLException("필수값 입력 오류입니다 : PASSWORD");
            }

        }catch (Exception e) {
            e.getStackTrace();
        }



        loginInfoTableDAO = mapperClass.inquiryUserInfo(loginRequestDTO.getUserId());
        LoginInfoTableDAO resultData = loginInfoTableDAO.get(0);
        System.out.println("userid1 : " + resultData.getUserId() +  "password" + resultData.getPassword());
        System.out.println("userid2 : " + userId +  "password" + password);

        if(!userId.equals(resultData.getUserId()) ) {
            //아이디 혹은 비밀번호 오류입니다.
            loginResponseDTO.setResponse("402");
        }else if(!password.equals(resultData.getPassword())) {

            //아이디 혹은 비밀번호 오류입니다.
            loginResponseDTO.setResponse("402");
        }else {
            loginInfoTableDAOParam.setLoginStatus("Y");
            loginResult = mapperClass.updateUserInfo("Y", resultData.getUserId());
            if(loginResult == 1) {
                loginResponseDTO.setUserId(resultData.getUserId());
                loginResponseDTO.setUserName(resultData.getUserName());
                loginResponseDTO.setResponse("200");
            }else {
                loginResponseDTO.setResponse("405");
            }

        }

        result.put("result", loginResponseDTO);
        return result;
    }

    @Override
    public HashMap<String, LoginResponseDTO> logoutUser(LoginRequestDTO loginRequestDTO) {
        HashMap<String, LoginResponseDTO> result = new HashMap<String, LoginResponseDTO>();
        List<LoginInfoTableDAO> loginInfoTableDAO = null;
        LoginInfoTableDAO loginInfoTableDAOParam = new LoginInfoTableDAO();
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        String userId = loginRequestDTO.getUserId();
        int logoutResult = 0;
        try {
            loginInfoTableDAO = mapperClass.inquiryUserInfo(loginRequestDTO.getUserId());
            LoginInfoTableDAO resultData = loginInfoTableDAO.get(0);
            //로그아웃 사용자 검증
            if(!userId.equals(resultData.getUserId()) ) {
                //아이디 혹은 비밀번호 오류입니다.
                loginResponseDTO.setResponse("402");
            }else {
                logoutResult = mapperClass.updateUserInfo("N", resultData.getUserId());
                if(logoutResult == 1) {
                    loginResponseDTO.setResponse("200");
                }else {
                    throw new SQLException("로그아웃 처리가 정상적으로 되지 않았습니다.");
                }

            }
        }catch (Exception e) {
            e.getStackTrace();
        }

        result.put("result", loginResponseDTO);
        return result;
    }


}
