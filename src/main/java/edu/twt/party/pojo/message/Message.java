package edu.twt.party.pojo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站内信，信件内容
 *
 * @author xsr
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private int messageId;
    private String title;  // 信件标题
    private String content;  // 信件内容（文字内容）
    private String attachmentPath;  // 附件路径（如不含附件则为空）
    private String picturePath;  // 图片地址（如不含图片为空）
    private String createdTime;
    private String updatedTime;
    private int isDeleted;
}
