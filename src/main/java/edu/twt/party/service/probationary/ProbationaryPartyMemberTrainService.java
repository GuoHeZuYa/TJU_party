package edu.twt.party.service.probationary;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberTrain;
import edu.twt.party.pojo.probationary.TrainDTO;
import edu.twt.party.service.probationary.impl.PPMConfig;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.poi.ss.formula.functions.PPMT;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProbationaryPartyMemberTrainService extends IService<ProbationaryPartyMemberTrain> {
    ProbationaryPartyMemberTrain getTrainByTerm(Integer term);

    Integer delTrainByTerm(Integer term);

    @Transactional
    Integer addTrain(TrainDTO trainDTO);

    Boolean updateSelectStateByTerm(Integer term, Boolean canSelect);

    Boolean updateOpenStateByTerm(Integer term, Boolean isOpen);
    Boolean updateFinishStateByTerm(Integer term, Boolean isFinished);
    List<ProbationaryPartyMemberTrain> getExamList(Integer user_type);

    Boolean getStateByTermAndOpt(Integer term, Integer opt);

    Integer getLatestTrain(Integer usertType);
}
