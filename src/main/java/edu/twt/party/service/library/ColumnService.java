package edu.twt.party.service.library;

import edu.twt.party.pojo.library.Column;
import edu.twt.party.pojo.library.ColumnInfo;
import edu.twt.party.pojo.library.LibraryFile;

import java.util.List;

/**
 * @author xsr
 * 栏目及文章Service
 */
public interface ColumnService {
    /**
     * 添加栏目接口，如果不是在尾部添加，所添加的栏目后的其他栏目自动向后移动一位
     *
     * @param columnName 栏目名称
     * @param position 栏目位置
     * @return boolean
     */
    boolean addNewColumn(String columnName, int position);

    /**
     * 修改栏目名称接口
     *
     * @param newName 新的栏目名称
     * @param columnId 栏目id
     * @return boolean
     */
    boolean alterColumnName(String newName, int columnId);

    /**
     *删除栏目接口，删除后后面的栏目自动向前移动一位
     *
     * @param columnId 待删除的栏目id
     * @return boolean
     */
    boolean deleteColumn(int columnId);

    /**
     * 修改栏目顺序接口，将id为columnId的栏目移动到newPosition的位置
     *
     * @param columnId 栏目id
     * @param newPosition 栏目新的位置
     * @return boolean
     */
    boolean alterColumnPosition(int columnId, int newPosition);

    /**
     * 获取所有栏目及相关信息
     *
     * @return List
     */
    List<ColumnInfo> getColumns();

    /**
     *
     * @param columnId 栏目id
     * @return List
     */
    List<LibraryFile> getFileOfColumn(int columnId);

    /**
     *
     * @param title 文件名称
     * @param address 文件路径
     * @param columnId 栏目id
     * @return boolean
     */
    boolean addFileToColumn(String title, String address, int columnId);

    /**
     *
     * @param fileId 文件id
     * @return boolean
     */
    boolean deleteFileFromColumn(int fileId);
}
