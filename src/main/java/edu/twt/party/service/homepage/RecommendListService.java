package edu.twt.party.service.homepage;

import edu.twt.party.pojo.homepage.RecommendListUnit;

import java.util.List;

/**
 * 推荐列表
 */
public interface RecommendListService {
    /**
     * 获取推荐列表
     *
     * @return List
     */
    List<RecommendListUnit> getRecommendList();

    /**
     * 向推荐列表中添加文件（如果不是在尾部添加，所添加的文件后的其他文件自动向后移动一位）
     *
     * @param fileId 文件在资源库中的id
     * @param position 文件在推荐列表中的位置
     * @return boolean
     */
    boolean addRecommendedFile(Integer fileId, Integer position);

    /**
     * 向列表头部添加文件
     *
     * @param fileId 文件对应在资源库中的id
     * @return boolean
     */
    boolean addRecommendFileToHead(Integer fileId);

    /**
     * 置顶推荐列表中的文件
     *
     * @param recommendFileId 要置顶的推荐列表文件
     * @return boolean
     */
    boolean makeTop(Integer recommendFileId);

    /**
     * 调整推荐列表中文件的顺序，带调整的文件变动后其他文件自动向前或向后补位
     *
     * @param recommendedFileId 要调整的文件的id（推荐列表中的id）
     * @param newPosition 文件调整后新的位置
     * @return boolean
     */
    boolean alterRecommendListOrder(Integer recommendedFileId, Integer newPosition);

    /**
     * 修改推荐列表中的文件
     *
     * @param recommendFileId 推荐列表中的文件对应推荐列表中的id
     * @param newId 新的文件在资源库中的id
     * @return boolean
     */
    boolean alterRecommendFile(Integer recommendFileId, Integer newId);

    /**
     * 删除推荐列表中的文件
     *
     * @param recommendFileId 文件在推荐列表中对应的id
     * @return boolean
     */
    boolean deleteRecommendFile(Integer recommendFileId);
}
