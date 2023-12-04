package edu.twt.party.service.library.impl;

import edu.twt.party.dao.library.LibraryMapper;
import edu.twt.party.exception.APIException;
import edu.twt.party.pojo.library.LibraryFile;
import edu.twt.party.service.library.LibraryFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = Throwable.class)
public class LibraryFileServiceImpl implements LibraryFileService {

    @Resource
    LibraryMapper libraryMapper;

    /**
     * 向资源库中添加文件
     *
     * @param text 文章内容
     * @param columnId 栏目id
     * @param path     文件上传后的路径(先调用文件上传接口)
     * @param title    文件在资源库中的名称
     * @return Boolean
     */
    @Override
    public Boolean addLibraryFile(String text, Integer columnId, String path, String title) throws APIException {
        try {
            libraryMapper.addLibraryFile(text, columnId, path, title);
            return true;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("添加资源库文件错误");
        }
    }

    /**
     * 获取某一栏目下的文件
     *
     * @param columnId 栏目id
     * @return LibraryFile类的List(按照时间顺序返回)
     */
    @Override
    public List<LibraryFile> getColumnFile(Integer columnId,int startNum) {
        try {
            return libraryMapper.getLibraryFileByColumnId(columnId,startNum);
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取资源库文件错误");
        }
    }

    /**
     * 删除文件
     *
     * @param fileId 要删除的文件id
     * @return Boolean
     */
    @Override
    public Boolean delete(Integer fileId) {
        try {
            libraryMapper.deleteLibraryFile(fileId);
            return true;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("删除资源库文件错误");
        }
    }

    /**
     * 通过id获取资源库中的文件
     *
     * @param fileId 文件在资源库中的id
     * @return LibraryFile类
     */
    @Override
    public LibraryFile getFileById(Integer fileId) {
        try {
            return libraryMapper.getLibraryFileById(fileId);
        } catch(APIException e) {
            throw e;
        } catch(Exception e) {
            e.printStackTrace();
            throw new APIException("获取资源库文件错误");
        }
    }

    /**
     * 模糊搜索
     *
     * @param name 搜索内容
     * @return LibraryFile类的List作为搜索结果(按照时间顺序返回)
     */
    @Override
    public List<LibraryFile> search(String name) {
        try {
            return libraryMapper.search(name);
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("查询资源库文件错误");
        }
    }

    /**
     * 修改资源库文件
     *
     * @param libraryFileId 要修改的资源库文件id
     * @param text          修改后的文章内容
     * @param path          修改后的附件路径
     * @param title         修改后的文章标题
     * @return Boolean
     */
    @Override
    public Boolean alterLibraryFile(Integer libraryFileId, String text, String path, String title) throws APIException {
        try {
            return libraryMapper.alterLibraryFile(libraryFileId, text, path, title) == 1;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("修改资源库文件错误");
        }
    }

    @Override
    public Integer getLibraryFileCount(int id) {
        return libraryMapper.getLibraryFileCount(id);
    }
}
