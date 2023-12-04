package edu.twt.party;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import edu.twt.party.pojo.Operation;
import edu.twt.party.pojo.user.NameSno;
import edu.twt.party.service.OperationRecordService;
import edu.twt.party.utils.ESService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest
class PartyApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void excelReadTest(){
    }

    @Resource
    ESService esService;


    String indexName = "manager_operation";
    @Test
    void addIndex() throws Exception {
        Assertions.assertTrue(esService.indexExists(indexName));
    }

    @Test
    void addDocument() throws Exception{

        Operation operation = new Operation("1","root2","root2退出",new Date());
        esService.createDocument(indexName,operation);
    }

    @Resource
    OperationRecordService operationRecordService;
//    @Test
//    void query() throws Exception{
//        // Object o = operationRecordService.search("manager_operation","operator","root2");
//        Object o2 = operationRecordService.fuzzySearch("description","退出");
//        System.out.println(o2);
//    }

}
