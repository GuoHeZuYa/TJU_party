package edu.twt.party.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName: Operation
 * @Description:
 * @Author: 过河卒
 * @Date: 2023/2/15 16:09
 * @Version: 1.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Operation {
    String id;
    String operator;
    String description;
    Date createdAt;
}
