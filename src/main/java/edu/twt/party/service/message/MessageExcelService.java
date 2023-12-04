package edu.twt.party.service.message;

import edu.twt.party.pojo.message.MessageUnit;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

public interface MessageExcelService {
    HashMap<String,Object> parseExcel(MultipartFile file) throws IOException;
    Boolean modifyExcelItem(int id,String name,String sno);
    HashMap<String,Object> commitMessage(int uid, int importId, MessageUnit messageUnit);
}
