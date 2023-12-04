package edu.twt.party.pojo.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    //主键
    private int id;
    //名字
    private String saveName;
    //上传者
    private int uploaderId;
    //文件类型
    private FileType type;
    //other
    @JsonIgnore
    private Date createAt;
    @JsonIgnore
    private Date deleteAt;
    @JsonIgnore
    private boolean isDeleted;
}
