package edu.twt.party.controller.probationary;

import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.probationary.ProbationaryPartyMemberTrain;
import edu.twt.party.pojo.probationary.TrainDTO;
import edu.twt.party.service.probationary.ProbationaryPartyMemberTrainService;
import edu.twt.party.service.probationary.impl.PPMConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "预备党员培训")
@RestController
@RequestMapping("/api/probationary/train")
@CrossOrigin
public class ProbationaryPartyMemberTrainController {
    @Autowired
    private ProbationaryPartyMemberTrainService trainService;

    @PostMapping("")
    @Operation(summary = "添加培训")
    public ResponseHelper<Integer> addTrain(@RequestBody TrainDTO train) {
        return new ResponseHelper<>(trainService.addTrain(train));
    }
    @GetMapping("/{term}")
    @Operation(summary = "按期数查询培训")
    public ResponseHelper<ProbationaryPartyMemberTrain> getTrainByTerm(@PathVariable Integer term) {
        return new ResponseHelper<>(trainService.getTrainByTerm(term));
    }

    @GetMapping
    @Operation(summary = "查询所有培训")
    public ResponseHelper<List<ProbationaryPartyMemberTrain>> getTrains() {
        return new ResponseHelper<>(trainService.getExamList(PPMConfig.USERT_TYPE));
    }
    @GetMapping("/latest")
    @Operation(summary = "获取最新一期培训", description = "获取最新一期未结束的培训的期数，若没有新培训则返回null")
    public ResponseHelper<Integer> getLatestTrain() {
        return new ResponseHelper(trainService.getLatestTrain(PPMConfig.USERT_TYPE));
    }
    @PostMapping("/del/{term}")
    @Operation(summary = "按期数删除培训")
    public ResponseHelper<Integer> delTrainByTerm(@PathVariable Integer term) {
        return new ResponseHelper<>(trainService.delTrainByTerm(term));
    }
    @PostMapping("/update")
    @Operation(summary = "按期数修改培训信息")
    public ResponseHelper<Boolean> updateTrainByTerm(@RequestBody ProbationaryPartyMemberTrain train) {
        return new ResponseHelper<>(trainService.updateById(train));
    }
    @PostMapping("/select/{term}/{canSelect}")
    @Operation(summary = "按期数修改能否选课" ,description = "1代表开放选课，0代码关闭选课")
    public ResponseHelper<Boolean> updateSelectStateByTerm(@PathVariable Integer term, @PathVariable Boolean canSelect) {
        return new ResponseHelper<>(trainService.updateSelectStateByTerm(term, canSelect));
    }
    @PostMapping("/open/{term}/{isOpen}")
    @Operation(summary = "按期数修改是否开放培训" ,description = "1代表开放培训，0代表关闭培训")
    public ResponseHelper<Boolean> updateOpenStateByTerm(@PathVariable Integer term, @PathVariable Boolean isOpen) {
        return new ResponseHelper<>(trainService.updateOpenStateByTerm(term, isOpen));
    }
    @PostMapping("/finish/{term}/{isFinished}")
    @Operation(summary = "按期数结束培训", description = "1代表结束培训,0代表未结束培训,如果能选课或者未关闭培训则不能关闭")
    public ResponseHelper<Boolean> updateFinishStateByTerm(@PathVariable Integer term, @PathVariable Boolean isFinished) {
        return new ResponseHelper<>(trainService.updateFinishStateByTerm(term, isFinished));
    }

    @GetMapping("/state/{term}/{opt}")
    @Operation(summary = "按期数查询某一状态", description = "1代表开放状态,2代表选课状态,3代表培训是否结束,未知选项返回null")
    public ResponseHelper<Boolean> getStateByTermAndOpt(@PathVariable Integer term, @PathVariable Integer opt) {
        return new ResponseHelper<>(trainService.getStateByTermAndOpt(term, opt));
    }


}
