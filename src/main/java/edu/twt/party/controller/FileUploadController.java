package edu.twt.party.controller;

import cn.hutool.json.JSONObject;
import edu.twt.party.annotation.JwtToken;
import edu.twt.party.constant.RoleType;
import edu.twt.party.dao.TwtManagerDao;
import edu.twt.party.dao.TwtStudentInfoMapper;
import edu.twt.party.helper.ResponseCode;
import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.file.File;
import edu.twt.party.pojo.file.HTD;
import edu.twt.party.pojo.file.HTDVo;
import edu.twt.party.pojo.student.TwtStudentInfo;
import edu.twt.party.service.file.FileUploadService;
import edu.twt.party.service.file.HTDService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;

@RestController
@Tag(name = "文件上传")
public class FileUploadController {
    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    HTDService htdService;
    @Autowired
    TwtStudentInfoMapper twtStudentInfoMapper;

    @PostMapping("/api/file/upload")
    @Operation(summary = "上传")
    public ResponseHelper<String> upload(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request){
        if(multipartFile == null){
            return new ResponseHelper<>(ResponseCode.FAILED,"");
        }
        return new ResponseHelper<>(fileUploadService.saveFile(multipartFile,1));
    }
    @GetMapping("api/file/all")
    @Operation(summary = "文件列表")
    public ResponseHelper<List<File>> getFiles(){
        return new ResponseHelper<List<File>>(fileUploadService.getAllFile());
    }
    @GetMapping("api/file/{id}/get")
    @Operation(summary = "获取文件直连")
    public ResponseHelper<String> getFileUrl(@PathVariable(name = "id") Integer id){
        return new ResponseHelper<>(fileUploadService.getFileUrl(id));
    }
    @GetMapping("api/file/{id}/delete")
    @Operation(summary = "删除文件")
    public ResponseHelper<Boolean> deleteFile(@PathVariable("id") Integer id){
        return new ResponseHelper<>(fileUploadService.deleteFile(id));
    }
    @GetMapping("/d/{type}/{name}")//如何实现通配符？？PathVariable不支持多个/
    @Operation(summary = "文件下载")
    public void downLoadFile(@PathVariable(name = "type")String type,@PathVariable(name = "name")String name,HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(name.contains("..")||type.contains(".."))return;
        response.setCharacterEncoding("utf-8");
        //定义文件路径
        java.io.File file = new java.io.File("twt_party_upload/"+type+"/"+ URLEncoder.encode(name,"utf-8"));
        InputStream is = null;
        OutputStream os = null;
        try {
            //分片下载
            long fSize = file.length();//获取长度
            response.setContentType("application/x-download");
            String fileName = URLEncoder.encode(file.getName(),"utf-8");
            response.addHeader("Content-Disposition","attachment;filename="+fileName);
            //根据前端传来的Range  判断支不支持分片下载
            response.setHeader("Accept-Range","bytes");
            //获取文件大小
            response.setHeader("fSize",String.valueOf(fSize));
            response.setHeader("fName",fileName);
            //定义断点
            long pos = 0,last = fSize-1,sum = 0;
            //判断前端需不需要分片下载
            if (null != request.getHeader("Range")){
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                String numRange = request.getHeader("Range").replaceAll("bytes=","");
                String[] strRange = numRange.split("-");
                if (strRange.length == 2){
                    pos = Long.parseLong(strRange[0].trim());
                    last = Long.parseLong(strRange[1].trim());
                    //若结束字节超出文件大小 取文件大小
                    if (last>fSize-1){
                        last = fSize-1;
                    }
                }else {
                    //若只给一个长度  开始位置一直到结束
                    pos = Long.parseLong(numRange.replaceAll("-","").trim());
                }
            }
            long rangeLength = last-pos+1;
            String contentRange = new StringBuffer("bytes").append(pos).append("-").append(last).append("/").append(fSize).toString();
            response.setHeader("Content-Range",contentRange);
            response.setHeader("Content-Length",String.valueOf(rangeLength));
            os = new BufferedOutputStream(response.getOutputStream());
            is = new BufferedInputStream(Files.newInputStream(file.toPath()));
            is.skip(pos);//跳过已读的文件
            byte[] buffer = new byte[1024];
            int lenght = 0;
            //相等证明读完
            while (sum < rangeLength){
                lenght = is.read(buffer,0, (rangeLength-sum)<=buffer.length? (int) (rangeLength - sum) :buffer.length);
                sum = sum+lenght;
                os.write(buffer,0,lenght);
            }
        }catch (IOException e){
            response.setStatus(404);
        }
        finally {
            if (is!= null){
                is.close();
            }
            if (os!=null){
                os.close();
            }
        }
    }
    @GetMapping("/api/file/htd/user/{id}")
    @JwtToken(roles = RoleType.COMMON_USER)//支书
    public ResponseHelper<List<HTD>> getHasCommitByUserId(@PathVariable("id") int id,HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        TwtStudentInfo targetUser = twtStudentInfoMapper.selectById(id);
        if(targetUser!=null && branchId == targetUser.getPartybranchId()){
            return new ResponseHelper<>(htdService.getHasCommitByUserId(id));
        }
        return new ResponseHelper<>(ResponseCode.FAILED,false,"无权限");
    }

    @GetMapping("/api/file/htd/{userId}/{type}")
    @JwtToken(roles = RoleType.COMMON_USER)//支书
    public ResponseHelper<HTD> getHTDByUserIdAndType(@PathVariable int userId,@PathVariable int type,HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        TwtStudentInfo targetUser = twtStudentInfoMapper.selectById(userId);
        if(targetUser!=null && branchId == targetUser.getPartybranchId()){
            return new ResponseHelper<>(htdService.getHTDByUserIdAndType(userId,type));
        }
        return new ResponseHelper<>(ResponseCode.FAILED,false,"无权限");
    }

    @PostMapping("/api/file/htd/status/{userId}/{type}")
    @JwtToken(roles = RoleType.COMMON_USER)//支书
    public ResponseHelper<Boolean> updateHTDStatus(@PathVariable int userId,@PathVariable int type,@RequestBody JSONObject body,HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        TwtStudentInfo targetUser = twtStudentInfoMapper.selectById(userId);
        if(targetUser!=null && branchId == targetUser.getPartybranchId()){
            if(body.getInt("status",-1)>=0){
                return new ResponseHelper<>( htdService.approvalHTD(userId,type, body.getStr("comment",""), body.getInt("status")));
            }else {
                return new ResponseHelper<>(ResponseCode.FAILED,false,"参数错误：status");
            }
        }
        return new ResponseHelper<>(ResponseCode.FAILED,false,"无权限");
    }
    @GetMapping("/api/file/htd/unread")
    @JwtToken(roles = RoleType.COMMON_USER)//支书
    public ResponseHelper<List<HTDVo>> getUnreadHTD(HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        int branchId = user.getPartybranchId();
        return new ResponseHelper<>(htdService.getUnread(branchId));

    }

    @PostMapping("/api/file/htd/upload/{type}")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<Boolean> insertHTD(HttpServletRequest request,@PathVariable int type,@RequestBody String content){
        return new ResponseHelper<>(htdService.insertHTD(((TwtStudentInfo)request.getAttribute("user")).getId(), type, content));
    }
    @GetMapping("/api/file/htd/my")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<List<HTD>> getMyCommitHTD(HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        return new ResponseHelper<>(htdService.getHasCommitByUserId(user.getId()));
    }
    @GetMapping("/api/file/htd/my/{type}")
    @JwtToken(roles = RoleType.COMMON_USER)
    public ResponseHelper<HTD> getMyHTDByType(@PathVariable int type,HttpServletRequest request){
        TwtStudentInfo user = (TwtStudentInfo) request.getAttribute("user");
        return new ResponseHelper<>(htdService.getHTDByUserIdAndType(user.getId(),type));
    }

}
