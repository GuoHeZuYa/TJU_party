package edu.twt.party.service.message.impl;

import edu.twt.party.dao.message.MessageMapper;
import edu.twt.party.pojo.message.Message;
import edu.twt.party.pojo.message.MessageImport;
import edu.twt.party.pojo.message.MessageUnit;
import edu.twt.party.service.message.MessageExcelService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@Transactional(rollbackFor = Throwable.class)
public class MessageExcelServiceImpl implements MessageExcelService {
    @Autowired
    MessageMapper messageMapper;
    public HashMap parseExcel(MultipartFile file) throws IOException {
        ArrayList<String> error = new ArrayList<>();
        HashMap<String,Object> ret = new HashMap<>();
        ArrayList<MessageImport> source = new ArrayList<>();
        ArrayList<MessageImport> results = new ArrayList<>();

        Workbook workbook;
        if(file.getOriginalFilename().endsWith("xlsx")){
            workbook = new XSSFWorkbook(file.getInputStream());
        }else {
            workbook = new HSSFWorkbook(file.getInputStream());
        }
        Sheet sheet = workbook.getSheet("Sheet1");
        if(sheet==null){
            error.add("请使用正确的模板");
            ret.put("importId",null);
            ret.put("list",null);
            ret.put("error",error);
            return ret;
        }
        int line = 0;
        for (Row row : sheet) {
            if (line == 0) {
                String[] columnName = {"学号", "姓名"};
                int cellNum = 0;
                for (String s : columnName) {
                    if (!s.equals(row.getCell(cellNum).getStringCellValue())) {
                        error.add("请使用正确的模板");
                        ret.put("importId", null);
                        ret.put("list", null);
                        ret.put("error", error);
                        return ret;
                    }
                    cellNum++;
                }
            } else if (line > 0) {
                source.add(new MessageImport(null, null, null,
                        row.getCell(0).getCellType()== CellType.NUMERIC?
                                String.valueOf(new Double(row.getCell(0).getNumericCellValue()).longValue()):
                                row.getCell(0).getStringCellValue(),
                        row.getCell(1).getStringCellValue(), null));
            }
            line++;
        }
        int importId = messageMapper.getNewImportId();
        if(messageMapper.importExcelUserList(source, importId)==0){
            throw new IOException("数据为空");
        }

        if(messageMapper.updateExcelUserListByImportId(importId)==0){
            throw new IOException("数据导入失败");
        }
        results = messageMapper.getImportExcelByImportId(importId);
        ret.put("importId",importId);
        ret.put("list",results);
        line = 1;
        for (MessageImport item :
                results) {
            if (item.getUserId() == null) {
                error.add("第"+line+"行数据不匹配");
            }
            line++;
        }

        ret.put("error",error);
        return ret;
    }

    @Override
    public Boolean modifyExcelItem(int id, String name, String sno){
        return messageMapper.updateExcelItem(id, name, sno);
    }
    public HashMap<String,Object> commitMessage(int uid, int importId, MessageUnit messageUnit){
        messageMapper.updateExcelUserListByImportId(importId);
        ArrayList<MessageImport> results = messageMapper.getImportExcelByImportId(importId);
        ArrayList<String> error = new ArrayList<>();
        HashMap<String,Object> ret = new HashMap<>();
        int line = 1;
        for (MessageImport item :
                results) {
            if (item.getUserId() == null) {
                error.add("第"+line+"行数据不匹配");
            }
            line++;
        }
        if(error.size()>0){
            ret.put("error",error);
            ret.put("result", Boolean.FALSE);
        }else {
            int receiverId = -2000-importId;
            Message message = messageUnit.convertToMessage();
            messageMapper.addMessage(message);
            int a = messageMapper.addAdminMessage(uid, receiverId, 0, message.getMessageId(), -1);
            ret.put("error",error);
            ret.put("result", a == 1);
        }

        return ret;
    }
}
