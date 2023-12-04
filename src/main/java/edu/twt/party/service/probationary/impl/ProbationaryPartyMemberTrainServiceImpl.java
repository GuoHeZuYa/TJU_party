package edu.twt.party.service.probationary.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.twt.party.dao.exam.ExamMapper;
import edu.twt.party.dao.probationary.ProbationaryPartyMemberTrainMapper;
import edu.twt.party.pojo.exam.Exam;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberTrain;
import edu.twt.party.pojo.probationary.TrainDTO;
import edu.twt.party.service.probationary.ProbationaryPartyMemberTrainService;
import org.apache.poi.ss.formula.functions.PPMT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProbationaryPartyMemberTrainServiceImpl extends ServiceImpl<ProbationaryPartyMemberTrainMapper, ProbationaryPartyMemberTrain>
    implements ProbationaryPartyMemberTrainService {
    @Autowired
    private ProbationaryPartyMemberTrainMapper trainMapper;

    @Override
    public ProbationaryPartyMemberTrain getTrainByTerm(Integer term) {
        ProbationaryPartyMemberTrain train = trainMapper.getExamByTerm(term, PPMConfig.USERT_TYPE);
        return train;
    }

    @Override
    public Integer delTrainByTerm(Integer term) {
        return trainMapper.delete(new QueryWrapper<ProbationaryPartyMemberTrain>().eq("times",term).eq("user_type",PPMConfig.USERT_TYPE));
    }

    @Override
    @Transactional
    public Integer addTrain(TrainDTO trainDTO) {
        ProbationaryPartyMemberTrain train = new ProbationaryPartyMemberTrain();
        train.setName(String.format("第%d期预备党员培训",trainDTO.getTerm()));
        train.setContent(trainDTO.getContent());
        train.setStatus(0);
        train.setTerm(trainDTO.getTerm());
        train.setStartTime(trainDTO.getStartTime());
        train.setEndTime(trainDTO.getEndTime());
        train.setUserType(PPMConfig.USERT_TYPE);
        Integer res = trainMapper.insert(train);
        return res;
    }

    @Transactional
    @Override
    public Boolean updateSelectStateByTerm(Integer term, Boolean canSelect) {
        ProbationaryPartyMemberTrain train = trainMapper.getExamByTerm(term, PPMConfig.USERT_TYPE);
        train.setSelect(canSelect);
        return trainMapper.updateById(train) == 1;
    }

    @Transactional
    @Override
    public Boolean updateOpenStateByTerm(Integer term, Boolean isOpen) {
        ProbationaryPartyMemberTrain train = trainMapper.getExamByTerm(term, PPMConfig.USERT_TYPE);
        train.setOpen(isOpen);
        return trainMapper.updateById(train) == 1;
    }

    @Transactional
    @Override
    public Boolean updateFinishStateByTerm(Integer term, Boolean isFinished) {
        ProbationaryPartyMemberTrain train = trainMapper.getExamByTerm(term, PPMConfig.USERT_TYPE);
        if(train.isOpen() || train.canSelect()) {
            return false;
        }
        train.setFinished(isFinished);
        return trainMapper.updateById(train) == 1;
    }

    @Override
    public List<ProbationaryPartyMemberTrain> getExamList(Integer user_type) {
        if(user_type.compareTo(PPMConfig.USERT_TYPE) != 0) {
            return null;
        }
        return trainMapper.getExamList(user_type);
    }

    @Override
    public Boolean getStateByTermAndOpt(Integer term, Integer opt) {
        Integer status = trainMapper.getStatusByTerm(term, PPMConfig.USERT_TYPE);
        if(status == null) {
            return null;
        }
        Integer state = null;
        if(opt == 1) {
            state = PPMConfig.OPEN;
        }
        if(opt == 2) {
            state = PPMConfig.SELECT;
        }
        if(opt == 3) {
            state = PPMConfig.FINISH;
        }
        if(state == null) {
            return null;
        }
        return ProbationaryPartyMemberTrain.transStatus(status, state);
    }

    @Override
    public Integer getLatestTrain(Integer usertType) {
        ProbationaryPartyMemberTrain train = trainMapper.getLatestTrain(usertType);
        if(!train.isFinished()) {
            return train.getTerm();
        }
        return null;
    }

}
