package edu.twt.party.pojo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageImport {
    private Integer id;
    private Integer importId;
    private Integer userId;
    private String sno;
    private String name;
    private Boolean isDeleted;
}
