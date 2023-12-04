package edu.twt.party.service.homepage.impl;

import edu.twt.party.dao.homepage.NoticeMapper;
import edu.twt.party.dao.library.LibraryMapper;
import edu.twt.party.exception.APIException;
import edu.twt.party.pojo.homepage.NoticeOrder;
import edu.twt.party.pojo.library.LibraryFile;
import edu.twt.party.service.homepage.NoticeService;
import edu.twt.party.pojo.homepage.Notice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = Throwable.class)
public class NoticeServiceImpl implements NoticeService {

    @Resource
    NoticeMapper noticeMapper;

    @Resource
    LibraryMapper libraryMapper;

    /**
     * 在头部添加通知
     *
     * @param text  通知内容
     * @param title 通知标题
     * @param path  附件路径（可为空）
     * @return Boolean
     */
    @Override
    public Boolean addNoticeToHead(String text, String title, String path) throws APIException {
        try {
            LibraryFile libraryFile = new LibraryFile(null, NOTICE_COLUMN_ID, text, path, title, null, null, null);
            libraryMapper.insertLibraryFile(libraryFile);
            //将其他通知向后顺移
            List<NoticeOrder> noticeOrderList = noticeMapper.getNoticeOrder();
            for(NoticeOrder i : noticeOrderList) {
                noticeMapper.alterNoticePosition(i.getLibraryFileId(), i.getPosition() + 1);
            }
            noticeMapper.addNotice(libraryFile.getLibraryFileId(), 1);
            return true;
        } catch (APIException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("添加通知错误");
        }
    }

    /**
     * 将通知置顶
     *
     * @param libraryFileId 文件id（对应资源库）
     * @return Boolean
     */
    @Override
    public Boolean makeTop(Integer libraryFileId) {
        try {
            List<NoticeOrder> noticeOrderList = noticeMapper.getNoticeOrder();
            NoticeOrder toChange = noticeMapper.getNoticeOrderById(libraryFileId);
            for(NoticeOrder i : noticeOrderList) {
                if(i.getPosition() < toChange.getPosition()) {
                    noticeMapper.alterNoticePosition(i.getLibraryFileId(), i.getPosition() + 1);
                }
            }
            noticeMapper.alterNoticePosition(libraryFileId, 1);
            return true;
        } catch (APIException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("置顶通知错误");
        }
    }

    /**
     * 获取近期通知列表
     *
     * @return Notice类List
     */
    @Override
    public List<Notice> getNotice() {
        try {
            return noticeMapper.getNotice();
        } catch (APIException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取近期通知列表错误");
        }
    }

    /**
     * 删除通知
     *
     * @param libraryFileId 通知对应的资源库id
     * @return Boolean
     */
    @Override
    public Boolean deleteNotice(Integer libraryFileId) {
        try {
            int a, b;
            List<NoticeOrder> noticeOrderList = noticeMapper.getNoticeOrder();
            NoticeOrder toDelete = noticeMapper.getNoticeOrderById(libraryFileId);
            for(NoticeOrder i : noticeOrderList) {
                if(i.getPosition() > toDelete.getPosition()) {
                    noticeMapper.alterNoticePosition(i.getLibraryFileId(), i.getPosition() - 1);
                }
            }
            a = noticeMapper.deleteNoticePosition(libraryFileId);
            b = libraryMapper.deleteLibraryFile(libraryFileId);
            return (a == 1) && (b == 1);
        } catch (APIException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("删除通知错误");
        }
    }

    /**
     * 修改近期通知
     *
     * @param libraryFileId 要修改的通知对应的资源库id
     * @param text          修改后的通知内容
     * @param title         修改后的题目
     * @param path          修改后的附件路径
     * @return Boolean
     */
    @Override
    public Boolean alterNotice(Integer libraryFileId, String text, String title, String path) {
        try {
            return libraryMapper.alterLibraryFile(libraryFileId, text, path, title) == 1;
        } catch (APIException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("修改通知错误");
        }
    }
}
