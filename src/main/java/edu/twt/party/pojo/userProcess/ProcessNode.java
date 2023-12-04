package edu.twt.party.pojo.userProcess;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessNode {
    private Integer id;
    private String name;
    private Integer action;
    private String prev;
    private String next;
    @JsonIgnore
    private Date createAt;
    @JsonIgnore
    private Date deleteAt;
    @JsonIgnore
    private Boolean isDeleted;
}
