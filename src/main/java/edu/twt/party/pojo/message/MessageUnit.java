package edu.twt.party.pojo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站内信内容
 *
 * @author xsr
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageUnit {
    private String title;  // 信件标题
    private String content;  // 信件内容（文字内容）
    private String attachmentPath;  // 附件路径（如不含附件则为空）
    private String picturePath;  // 图片地址（如不含图片为空）

    public Message convertToMessage() {
        return new Message(0, title, content, attachmentPath, picturePath, null, null, 0);
    }
}
