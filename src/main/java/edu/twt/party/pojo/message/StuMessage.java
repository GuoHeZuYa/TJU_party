package edu.twt.party.pojo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuMessage {
    private int messageInfoId; // 信件id
    private String title;  // 信件标题
    private String content;  // 信件内容（文字内容）
    private String attachmentPath;  // 附件路径（如不含附件则为空）
    private String picturePath;  // 图片地址（如不含图片为空）
    private int senderId;  // 发信者id(普通用户)
    private int receiverId;  // 收信者id(管理员类型)
    private int handlerId;  // 处理人id(管理员表),id=0时表示未处理
    private int status;  // 0:未读; 1:已读
    private String sendTime;  // 信件发送时间

    StuMessage(StuMessageInfo messageInfo, Message message) {
        title = message.getTitle();
        content = message.getContent();
        attachmentPath = message.getAttachmentPath();
        picturePath = message.getPicturePath();
        senderId = messageInfo.getSenderId();
        receiverId = messageInfo.getReceiverId();
        status = messageInfo.getStatus();
        receiverId = messageInfo.getMessageId();
        status = messageInfo.getStatus();
        sendTime = messageInfo.getSendTime();
    }
}
