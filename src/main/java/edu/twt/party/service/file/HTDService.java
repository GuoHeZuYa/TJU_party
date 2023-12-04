package edu.twt.party.service.file;

import edu.twt.party.pojo.file.HTD;
import edu.twt.party.pojo.file.HTDVo;

import java.util.List;

public interface HTDService {
    List<HTD> getHasCommitByUserId(int userId);
    HTD getHTDByUserIdAndType(int userId, int type);
    Boolean insertHTD(int userId, int type, String content);
    Boolean updateHTDStatus(int userId,int type,int status);
    Boolean commentHTD(int userId,int type, String comment);
    Boolean approvalHTD(int userId,int type,String comment,int status);

    List getUnread(int partyBranchId);
}
