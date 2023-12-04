package edu.twt.party.pojo.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HTD {
    int id;
    int userId;
    int type;
    String content;
    int status;
    Date createAt;
    @JsonIgnore
    boolean isDelete;
    @JsonIgnore
    Date deleteAt;

    String comment;
}
