package edu.twt.party.pojo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMessage {
    private int messageInfoId; // 信件id
    private String title;  // 信件标题
    private String content;  // 信件内容（文字内容）
    private String attachmentPath;  // 附件路径（如不含附件则为空）
    private String picturePath;  // 图片地址（如不含图片为空）
    private int senderId;
    private int receiverId; // 发送非单独的对象为正数，即对象的id; 发送给群体对象为负数
    // 发送给所有普通用户为 -1，发送给学院的用户代码后续进行约定
    private int status;  // 0:未读; 1:已读
    private int replyMessageId; // 回复的普通用户发送的站内信的id，若为-1，则表示不是回复
    private String sendTime;

    AdminMessage(AdminMessageInfo messageInfo, Message message) {
        title = message.getTitle();
        content = message.getContent();
        attachmentPath = message.getAttachmentPath();
        picturePath = message.getPicturePath();
        senderId = messageInfo.getSenderId();
        receiverId = messageInfo.getReceiverId();
        status = messageInfo.getStatus();
        receiverId = messageInfo.getMessageId();
        status = messageInfo.getStatus();
        replyMessageId = messageInfo.getReplyMessageId();
        sendTime = messageInfo.getSendTime();
    }
}
