package edu.twt.party.service.homepage;

import edu.twt.party.pojo.homepage.Notice;

import java.util.List;

/**
 * 近期通知
 * @author xsr
 */
public interface NoticeService {

    int NOTICE_COLUMN_ID = 1; //通知对应的资源库栏目id

    /**
     * 在头部添加通知
     *
     * @param text 通知内容
     * @param title 通知标题
     * @param path 附件路径（可为空）
     * @return Boolean
     */
    Boolean addNoticeToHead(String text, String title, String path);

    /**
     * 将通知置顶
     *
     * @param libraryFileId 文件id（对应资源库）
     * @return Boolean
     */
    Boolean makeTop(Integer libraryFileId);

    /**
     * 获取近期通知列表
     *
     * @return Notice类List
     */
    List<Notice> getNotice();

    /**
     * 删除通知
     *
     * @param libraryFileId 通知对应的资源库id
     * @return Boolean
     */
    Boolean deleteNotice(Integer libraryFileId);

    /**
     * 修改近期通知
     *
     * @param libraryFileId 要修改的通知对应的资源库id
     * @param text 修改后的通知内容
     * @param title 修改后的题目
     * @param path 修改后的附件路径
     * @return Boolean
     */
    Boolean alterNotice(Integer libraryFileId, String text, String title, String path);
}
