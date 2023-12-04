package edu.twt.party.service.file;

import edu.twt.party.pojo.file.File;
import edu.twt.party.pojo.file.FileType;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.util.List;

public interface FileUploadService {
    FileType getFileType(InputStream inputStream,String fileName);
    String saveFile(MultipartFile multipartFile,int uploaderId);
    String getFileUrl(int id);
    List<File> getAllFile();
    boolean deleteFile(int id);
}