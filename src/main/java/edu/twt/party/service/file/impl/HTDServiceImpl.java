package edu.twt.party.service.file.impl;

import edu.twt.party.dao.file.HTDMapper;
import edu.twt.party.pojo.file.HTD;
import edu.twt.party.pojo.file.HTDVo;
import edu.twt.party.service.file.HTDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HTDServiceImpl implements HTDService {
    @Autowired
    HTDMapper htdMapper;
    public List<HTD> getHasCommitByUserId(int userId){
        List<HTD> arrayList = htdMapper.getHasCommitByUserId(userId);
        ArrayList<HTD> distinct = new ArrayList<>();
        if(arrayList.size()==0){
            return arrayList;
        }
        for(int i=0;i<arrayList.size()-1;i++){
            if(arrayList.get(i).getType()!=arrayList.get(i+1).getType()){
                distinct.add(arrayList.get(i));
            }
        }
        distinct.add(arrayList.get(arrayList.size()-1));
        for (HTD htd : distinct) {
            htd.setContent("");
        }
        return distinct;
    }
    public HTD getHTDByUserIdAndType(int userId,int type){
        return htdMapper.getHTDByUserIdAndType(userId,type);
    }
    public Boolean insertHTD(int userId, int type, String content){
        HTD htd = htdMapper.getHTDByUserIdAndType(userId, type);
        if(htd==null || htd.getStatus()==2){
            return htdMapper.insertHTD(userId, type, content);
        }else {
            return false;
        }
    }
    public Boolean updateHTDStatus(int userId,int type,int status){
        return htdMapper.updateHTDStatus(userId, type, status);
    }

    @Override
    public Boolean commentHTD(int userId, int type, String comment) {
        return htdMapper.commentHTDByUserIdAndType(userId, type, comment);
    }

    @Override
    public Boolean approvalHTD(int userId, int type, String comment, int status) {
        return htdMapper.commentHTDByUserIdAndType(userId,type,comment)&&updateHTDStatus(userId, type, status);
    }

    @Override
    public List<HTDVo> getUnread(int partyBranchId) {
        List<HTDVo> ret = htdMapper.getUnread(partyBranchId);
        for (HTDVo htdvo : ret) {
            htdvo.setContent("");
        }
        return ret;
    }
}
