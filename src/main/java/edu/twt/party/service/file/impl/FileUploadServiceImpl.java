package edu.twt.party.service.file.impl;

import edu.twt.party.dao.file.FileUploadMapper;
import edu.twt.party.exception.APIException;
import edu.twt.party.pojo.file.FileType;
import edu.twt.party.service.file.FileUploadService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    Tika tika = new Tika();
    @Autowired
    FileUploadMapper fileUploadMapper;
    private static final String UPLOAD_PATH = "twt_party_upload/";

    @Override
    public FileType getFileType(InputStream inputStream,String fileName){
        try {
            FileType fileType = FileType.getFileType(tika.detect(inputStream));
            if(fileType==FileType.ARCHIVE){
                if(fileName.endsWith("xlsx")||fileName.endsWith("docx")||fileName.endsWith("pptx")){//这仨magic是pk和zip一样
                    return FileType.DOCUMENT;
                }else {
                    return FileType.ARCHIVE;
                }
            }
            return fileType;
        }catch (IOException e){
            return FileType.UNKNOWN;
        }
    }


    @Override
    public String saveFile(MultipartFile multipartFile,int uploaderId) throws APIException{
//        File uploadDir = new File(UPLOAD_PATH);
        File uploadImageDir = new File(UPLOAD_PATH+"IMAGE/");
        File uploadDocumentDir = new File(UPLOAD_PATH+"DOCUMENT/");
        File uploadArchiveDir = new File(UPLOAD_PATH+"ARCHIVE/");
        File uploadUnknownDir = new File(UPLOAD_PATH+"UNKNOWN/");
        if(!uploadImageDir.exists()){
            if(!uploadImageDir.mkdirs()){
                throw new APIException("Upload dir not exist ,and create upload dir fail");
            }
        }
        if(!uploadDocumentDir.exists()){
            if(!uploadDocumentDir.mkdirs()){
                throw new APIException("Upload dir not exist ,and create upload dir fail");
            }
        }
        if(!uploadArchiveDir.exists()){
            if(!uploadArchiveDir.mkdirs()){
                throw new APIException("Upload dir not exist ,and create upload dir fail");
            }
        }
        if(!uploadUnknownDir.exists()){
            if(!uploadUnknownDir.mkdirs()){
                throw new APIException("Upload dir not exist ,and create upload dir fail");
            }
        }
        FileType fileType = FileType.UNKNOWN;
        try {
            fileType = getFileType(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        }catch (Exception e){
            throw new APIException("detect file type error");
        }
        String saveName;
        try {
            saveName = System.currentTimeMillis() + URLEncoder.encode(multipartFile.getOriginalFilename(), "utf-8");
            if(saveName.length()>255){
                saveName = saveName.substring(0,255);
            }
        }catch (Exception e){
            throw new APIException("encode name error");
        }
        File file = new File(System.getProperty("user.dir")+"/"+ UPLOAD_PATH + fileType.toString() + "/" + saveName);

        try {
            multipartFile.transferTo(file);//file必须用绝对路径,否则存到tmp目录
        }catch (IOException e){
            System.out.println(e);
            throw new APIException("File transfer fail");
        }

        if(fileUploadMapper.saveFile(saveName,uploaderId,fileType)){
            return fileType.toString() + "/" +saveName;
        }
        return "";
    }

    @Override
    public String getFileUrl(int id) {
        try{
            edu.twt.party.pojo.file.File f = fileUploadMapper.getFileById(id);
            if(f != null){
                return f.getType().toString() + "/"+ f.getSaveName();
            }
        }catch (NullPointerException e){
            throw new APIException("非法文件:源文件不存在");
        }
        return "";
    }


    @Override
    public List<edu.twt.party.pojo.file.File> getAllFile() {
        return fileUploadMapper.getAllFile();
    }

    @Override
    public boolean deleteFile(int id) {
        return fileUploadMapper.deleteFile(id);
    }

}
