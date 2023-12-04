package edu.twt.party.pojo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站内信（申诉）
 *
 * @author xsr
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuMessageInfo {
    private int messageInfoId;
    private int messageId; // 对应的信件内容的 id
    private int senderId;  // 发信者id(普通用户)
    private int receiverId;  // 收信者id(管理员类型)
    private int handlerId;  // 处理人id(管理员表),id=0时表示未处理
    private int status;  // 0:未读; 1:已读
    private String sendTime;  // 信件发送时间
    private Boolean isDeleted;
}
