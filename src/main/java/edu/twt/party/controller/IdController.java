package edu.twt.party.controller;

import edu.twt.party.dao.IdMapper;
import edu.twt.party.helper.ResponseCode;
import edu.twt.party.helper.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@Tag(name = "工具类")
public class IdController {

    @Autowired
    IdMapper idMapper;

    @GetMapping("/api/id/{sno}/getUserId")
    public ResponseHelper<Integer> getUserId(@PathVariable(name = "sno") String sno){
        return new ResponseHelper<>(idMapper.getUserIdBySno(sno));
    }

    @GetMapping("/api/id/{id}/getUserSno")
    public ResponseHelper<String> getUserSno(@PathVariable(name = "id")Integer id){
        return new ResponseHelper<>(idMapper.getUserSnoById(id));
    }

    @GetMapping("/api/utils/year")
    @Operation(summary = "获取年份列表,0表示没有年份的党支部")
    public ResponseHelper<List<Integer>> getYearList(){
        try {
            int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            List<Integer> yearList = new ArrayList<>();
            for(int i = 0;i<6;i++){
                yearList.add(thisYear-i);
            }
            yearList.add(0);
            return new ResponseHelper<>(yearList);
        }catch (Exception e){
            return new ResponseHelper<>(ResponseCode.ERROR,null);
        }
    }

}
