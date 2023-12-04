package edu.twt.party.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import edu.twt.party.constant.RoleType;
import edu.twt.party.dao.BUserinfoMapper;
import edu.twt.party.dao.CollegeMapper;
import edu.twt.party.dao.TwtPartybranchMapper;
import edu.twt.party.dao.TwtStudentInfoMapper;
import edu.twt.party.helper.ResponseCode;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.College;
import edu.twt.party.pojo.partyBranch.PartyBranchCreateReq;
import edu.twt.party.pojo.partyBranch.PartyBranchGroupVo;
import edu.twt.party.pojo.partyBranch.TwtPartyBranchVo;
import edu.twt.party.pojo.partyBranch.TwtPartybranch;
import edu.twt.party.pojo.student.BUserinfo;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.pojo.user.NameSno;
import edu.twt.party.service.OperationRecordService;
import edu.twt.party.utils.JwtUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: TwtPartyBranchImpl
 * @Description:
 * @Author: 过河卒
 * @Date: 2022/9/10 14:47
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class TwtPartyBranchService extends BaseService{

    @Resource
    TwtPartybranchMapper twtPartybranchMapper;

    @Resource
    TwtStudentInfoMapper twtStudentInfoMapper;

    @Resource
    BUserinfoMapper bUserinfoMapper;

    @Resource
    CollegeMapper collegeMapper;

    @Resource
    OperationRecordService operationRecordService;





    
    public ResponseHelper<Boolean> updateUserPartyBranch(String sno, Integer targetBranchId) {



        Integer uid = twtStudentInfoMapper.getIdBySno(sno);
        if (twtPartybranchMapper.selectById(targetBranchId)==null){
            return new ResponseHelper<Boolean>(ResponseCode.PARTY_BRANCH_NOT_EXITST,false);
        }
        try {
            TwtStudentInfo info = twtStudentInfoMapper.selectById(uid);
            Integer mainStatus = info.getMainStatus();
            Integer oldBranchId = info.getPartybranchId();

            //触发器保证了两边的信息同步
            twtStudentInfoMapper.updateById(new TwtStudentInfo().setId(uid).setPartybranchId(targetBranchId));

            String token = JwtUtils.getToken();
            String operatorNum = JwtUtils.AnalysisUserNumber(token);

            operationRecordService.log(operatorNum,operatorNum+" 将 "+sno+" 的党支部修改为 "+targetBranchId);

            return new ResponseHelper<>(true) ;
        }catch (Exception e){
            LOGGER.info("fail to update partyBranch,id = {}",uid,e);
            return new ResponseHelper<>(ResponseCode.FAILED,false);
        }
    }

    
    public ResponseHelper<Object> batchUpdateUserPartyBranch(List<NameSno> nameSnoList, Integer targetBranchId) {
        try {



            List<String> snoList = Lists.newArrayList();

            //校验学生情况
            for (NameSno nameSno:nameSnoList){
                String name  = nameSno.getName();
                String sno   = nameSno.getSno();
                //检查学号和姓名是否一致
                BUserinfo info = bUserinfoMapper.checkNameSno(name,sno);
                if (Objects.isNull(info)){
                    return new ResponseHelper(ResponseCode.WRONG_SNO_FOR_NAME,name+" not match "+sno);
                }

                //从党支部中踢出，不用检查
                if (targetBranchId!=0) {
                    //检查该同学是否是目标党支部学院的
                    Integer targetCollegeId = twtPartybranchMapper.selectById(targetBranchId).getPartybranchAcademy();
                    if (!targetCollegeId.equals(info.getCollegeid())) {
                        return new ResponseHelper<>(ResponseCode.WRONG_TARGET_BRANCH, name + "_" + sno + "不属于目标党支部所属学院");
                    }
                }
                snoList.add(sno);
            }

            return new ResponseHelper<>(twtPartybranchMapper.batchUpdateUserBranch(snoList,targetBranchId));

        }catch (Exception e){
            LOGGER.error("fail to batchUpdate");
            return ResponseHelper.error(ResponseCode.ERROR);
        }
    }

    
    public ResponseHelper<Integer> createPartyBranch(PartyBranchCreateReq req) {
        try {
            TwtPartybranch twtPartybranch = new TwtPartybranch();
            BeanUtils.copyProperties(req,twtPartybranch);
            return new ResponseHelper<>(twtPartybranchMapper.insert(twtPartybranch));
        }catch (Exception e){
            LOGGER.info("fail to create partyBranch",e);
            return new ResponseHelper<>(ResponseCode.FAILED,0);
        }
    }

    
    public ResponseHelper<TwtPartyBranchVo> getTwtPartyBranchInfo(Integer id) {
        try {

            TwtPartyBranchVo vo = twtPartybranchMapper.selectVoById(id);
            return new ResponseHelper<>(vo);
        }catch (Exception e){
            LOGGER.info("fail to get partyBranch",e);
            return new ResponseHelper<>(ResponseCode.FAILED,null);
        }
    }

    
    public ResponseHelper<Boolean> updatePartyBranch3Person(Integer branchId,Integer index, String sno) {
        try {
            TwtPartybranch info = new TwtPartybranch().setPartybranchId(branchId);
            Integer uid = twtStudentInfoMapper.getIdBySno(sno);
            if(sno.length()==0){
                uid = 0;
            }
            if(uid==null){
                return new ResponseHelper<>(ResponseCode.TARGET_NOT_EXIST,false);
            }
            String positionName = "";
            if(index.equals(0)){
                positionName = "支书";
                twtPartybranchMapper.updatePartybranchSecretary(branchId,uid);
            }else if(index.equals(1)){
                positionName = "组织委员";
                twtPartybranchMapper.updatePartybranchOrganizerInt(branchId,uid);
            }else if(index.equals(2)){
                positionName = "宣传委员";
                twtPartybranchMapper.updatePartyBranchPropogator(branchId,uid);
            }

            String operatorNum = JwtUtils.getUserNumber();
            String name = bUserinfoMapper.getUserName(sno);
            operationRecordService.log(operatorNum,
                    operatorNum+"将"+info.getPartybranchName()+"的"+positionName+"更改为"+name+"(学工号为:sno)");

            return new ResponseHelper<>(true);
        }catch (Exception e){
            LOGGER.info("党支部信息",e);
            return new ResponseHelper<>(ResponseCode.FAILED,false);
        }
    }

    
    public ResponseHelper<List<BUserinfo>> getBranchMemberList(Integer branchId) {
        try {

            return new ResponseHelper<>
                    (bUserinfoMapper.selectList(Wrappers.<BUserinfo>lambdaQuery().eq(BUserinfo::getPartybranchid,
                    branchId)));
        }catch (Exception e){
            LOGGER.info("fail to get memberList",e);
            return new ResponseHelper<>(ResponseCode.FAILED,null);
        }
    }

    
    public ResponseHelper<List<PartyBranchGroupVo>> getBranchList(Integer collegeId, String grade, String type) {
        try {

            if(grade.length()==0){
                grade = null;
            }
            if (type.length() == 0){
                type = null;
            }



            List<College> colleges = collegeMapper.getColleges();

            //todo:放redis里面
            Map<Integer,String> collegeMap =
                    colleges.stream().collect(Collectors.toMap(College::getId,College::getCollegeName));

            //如果为空就查询所有的
            List<TwtPartyBranchVo> list = twtPartybranchMapper.selectBranchList(collegeId,grade,type);


            Map<Integer,List<TwtPartyBranchVo>> map =
                    list.stream().collect(Collectors.groupingBy(TwtPartyBranchVo::getPartybranchAcademy));

            List<PartyBranchGroupVo> ret =
                    map.entrySet().stream()
                    .map(e->new PartyBranchGroupVo(
                            e.getKey(),
                            collegeMap.get(e.getKey()),
                            e.getValue().size(),
                            e.getValue()))
                    .collect(Collectors.toList());

            //如果是学院管理员只能查看到自己学院的信息
            //可以优化，放到前面，这里偷个懒QwQ

            Integer userType = JwtUtils.getUserType();

            if(userType.equals(RoleType.COLLEGE_ADMIN.getTypeNum())){
                ret = ret.stream()
                        .filter(item -> item.getCollegeId().equals(JwtUtils.getCollegeId()))
                        .collect(Collectors.toList());
            }


            return new ResponseHelper<>(ret);
        }catch (Exception e){
            LOGGER.warn("fail to get branchList",e);
            return new ResponseHelper<>(ResponseCode.FAILED,null);
        }
    }

    
    public ResponseHelper<Boolean> delete(Integer Id) {
        try {
            boolean ret = twtPartybranchMapper.delete(Id)==1;
            if(ret){
                return new ResponseHelper<>();
            }else {
                return new ResponseHelper<>(ResponseCode.TARGET_NOT_EXIST,false);
            }
        }catch (Exception e){
            return new ResponseHelper<>(ResponseCode.DATABASE_FAILED,false);
        }
    }

    
    public ResponseHelper<List<NameSno>> analysisExcel(MultipartFile file) {
        try {
            InputStream input = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(input);
            List<List<Object>> headRow = reader.read(0,0);
            if(!("name".equals(headRow.get(0).get(0))&& "sno".equals(headRow.get(0).get(1)))){
                LOGGER.warn("file format wrong");
                return ResponseHelper.error(ResponseCode.FILE_FORMAT_WRONG);
            }
            List<NameSno> all = reader.readAll(NameSno.class);
            return new ResponseHelper<>(all);

        }catch (Exception e){
            return new ResponseHelper<>(ResponseCode.ERROR,null);
        }
    }

    
    public ResponseHelper<Boolean> kickOut(List<Integer> uidList,Integer branchId) {
        try {

            return new ResponseHelper<>(twtPartybranchMapper.kickOut(uidList,branchId)==uidList.size());
        }catch (Exception e){
            return ResponseHelper.error();
        }
    }

}
