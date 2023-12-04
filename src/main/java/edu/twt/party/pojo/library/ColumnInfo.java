package edu.twt.party.pojo.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnInfo {
    private int columnId;
    private String name;
    private int position;  //栏目的位置，即当前栏目为第n个栏目
    private String createdTime;
    private String updatedTime;
    private int fileNum; //栏目中文件的数目
}
