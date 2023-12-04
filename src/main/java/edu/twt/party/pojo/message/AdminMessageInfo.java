package edu.twt.party.pojo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站内信（通知）（管理员发送）
 *
 * @author xsr
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMessageInfo {
    private int messageInfoId;
    private int messageId; // 对应的信件内容的 id
    private int senderId;
    private int receiverId; // 发送非单独的对象为正数，即对象的id; 发送给群体对象为负数
                            // 发送给所有普通用户为 -1，发送给学院的用户代码后续进行约定
    private int status;  // 0:未读; 1:已读
    private int replyMessageId; // 回复的普通用户发送的站内信的id，若为-1，则表示不是回复
    private String sendTime;
    private Boolean isDeleted;
}
