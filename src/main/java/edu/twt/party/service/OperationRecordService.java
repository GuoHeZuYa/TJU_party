package edu.twt.party.service;

import edu.twt.party.pojo.Operation;
import edu.twt.party.utils.ESService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: OperationRecordService
 * @Description:
 * @Author: 过河卒
 * @Date: 2023/2/15 16:59
 * @Version: 1.0
 */
@Service
@Slf4j
public class OperationRecordService {

    @Resource
    ESService esService;



    String indexName = "manager_operation";

    public void log(String managerNum,String operationDescription){
        try {
            Operation operation = new Operation();
            operation.setOperator(managerNum).setDescription(operationDescription).setCreatedAt(new Date());
            esService.createDocument(indexName,operation);
        }catch (Exception e){
            log.error("fail to log,maybe es_service is down");
        }

    }

    public List<Operation> exactSearch(String fieldName,String searchContent) throws IOException {
        return esService.exactSearch(indexName,fieldName,searchContent);
    }

    public List<Object> fuzzySearch(String fieldName,String searchContent) throws IOException {
        return esService.fuzzySearch(indexName,fieldName,searchContent);
    }



}
