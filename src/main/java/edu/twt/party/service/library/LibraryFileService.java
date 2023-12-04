package edu.twt.party.service.library;

import edu.twt.party.pojo.library.LibraryFile;

import java.util.List;

/**
 * 资源库
 *
 * @author xsr
 */
public interface LibraryFileService {
    /**
     * 向资源库中添加文件
     *
     * @param text 文章内容
     * @param columnId 栏目id
     * @param path 文件上传后的路径(先调用文件上传接口)
     * @param title 文件在资源库中的名称
     * @return Boolean
     */
    Boolean addLibraryFile(String text, Integer columnId, String path, String title);

    /**
     * 获取某一栏目下的文件
     *
     * @param columnId 栏目id
     * @return LibraryFile类的List(按照时间顺序返回)
     */
    List<LibraryFile> getColumnFile(Integer columnId,int startNum);

    /**
     * 删除文件
     *
     * @param fileId 要删除的文件id
     * @return Boolean
     */
    Boolean delete(Integer fileId);

    /**
     * 通过id获取资源库中的文件
     *
     * @param fileId 文件在资源库中的id
     * @return LibraryFile类
     */
    LibraryFile getFileById(Integer fileId);

    /**
     * 模糊搜索
     *
     * @param name 搜索内容
     * @return LibraryFile类的List作为搜索结果(按照时间顺序返回)
     */
    List<LibraryFile> search(String name);

    /**
     * 修改资源库文件
     *
     * @param libraryFileId 要修改的资源库文件id
     * @param text 修改后的文章内容
     * @param path 修改后的附件路径
     * @param title 修改后的文章标题
     * @return Boolean
     */
    Boolean alterLibraryFile(Integer libraryFileId, String text, String path, String title);
    Integer getLibraryFileCount(int id);
}
