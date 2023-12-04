package edu.twt.party.service.homepage.impl;

import edu.twt.party.dao.library.LibraryMapper;
import edu.twt.party.dao.homepage.RecommendListMapper;
import edu.twt.party.exception.APIException;
import edu.twt.party.pojo.library.LibraryFile;
import edu.twt.party.pojo.homepage.RecommendedFile;
import edu.twt.party.service.homepage.RecommendListService;
import edu.twt.party.pojo.homepage.RecommendListUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xsr
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class RecommendListServiceImpl implements RecommendListService {

    @Resource
    RecommendListMapper recommendListMapper;

    @Resource
    LibraryMapper libraryMapper;

    /**
     * 获取推荐列表
     *
     * @return List
     */
    @Override
    public List<RecommendListUnit> getRecommendList() {
        try {
            List<RecommendedFile> tmpList = recommendListMapper.getRecommendList();
            if(tmpList == null) {
                return null;
            }
            List<RecommendListUnit> recommendList = new ArrayList<>();
            for(RecommendedFile tmp : tmpList) {
                LibraryFile file = libraryMapper.getLibraryFileById(tmp.getFileId());
                recommendList.add(new RecommendListUnit(tmp.getRecommendedFileId(), file.getLibraryFileId(), file.getTitle(), tmp.getPosition()));
            }
            return recommendList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("获取推荐列表错误");
        }
    }

    /**
     * 向推荐列表中添加文件（如果不是在尾部添加，所添加的文件后的其他文件自动向后移动一位）
     *
     * @param fileId 文件在资源库中的id
     * @param position 文件在推荐列表中的位置
     * @return boolean
     */
    @Override
    public boolean addRecommendedFile(Integer fileId, Integer position) {
        try {
            List<RecommendedFile> presentList = recommendListMapper.getRecommendList();
            if((presentList!= null && position > presentList.size() + 1) || position <= 0) {
                throw new APIException("非法位置");
            }
            //将所添加文件之后的其他文件位置向后移动
            if(presentList != null && position < presentList.size() + 1) {
                for(RecommendedFile tmp : presentList) {
                    if(tmp.getPosition() >= position) {
                        recommendListMapper.alterRecommendedFilePosition(tmp.getPosition() + 1, tmp.getRecommendedFileId());
                    }
                }
            }
            recommendListMapper.addRecommendedFile(fileId, position);
            return true;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("添加文件错误");
        }
    }

    /**
     * 向列表头部添加文件
     *
     * @param fileId 文件对应在资源库中的id
     * @return boolean
     */
    @Override
    public boolean addRecommendFileToHead(Integer fileId) {
        try{
            List<RecommendedFile> presentList = recommendListMapper.getRecommendList();
            for(RecommendedFile tmp : presentList) {
                recommendListMapper.alterRecommendedFilePosition(tmp.getPosition() + 1, tmp.getRecommendedFileId());
            }
            recommendListMapper.addRecommendedFile(fileId, 1);
            return true;
        } catch(APIException e) {
            throw e;
        } catch(Exception e) {
            e.printStackTrace();
            throw new APIException("添加推荐列表文件失败");
        }
    }

    /**
     * 置顶推荐列表中的文件
     *
     * @param recommendFileId 要置顶的推荐列表文件
     * @return boolean
     */
    @Override
    public boolean makeTop(Integer recommendFileId) {
        try {
            List<RecommendedFile> presentList = recommendListMapper.getRecommendList();
            RecommendedFile toChange = recommendListMapper.getRecommendedFileById(recommendFileId);
            for(RecommendedFile tmp : presentList) {
                if(tmp.getPosition() < toChange.getPosition()) {
                    recommendListMapper.alterRecommendedFilePosition(tmp.getPosition() + 1, tmp.getRecommendedFileId());
                }
            }
            recommendListMapper.alterRecommendedFilePosition(1, toChange.getRecommendedFileId());
            return true;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("文件置顶操作错误");
        }
    }

    /**
     * 调整推荐列表中文件的顺序，待调整的文件变动后其他文件自动向前或向后补位
     *
     * @param recommendedFileId 要调整的文件的id（推荐列表中的id）
     * @param newPosition 文件调整后新的位置
     * @return boolean
     */
    @Override
    public boolean alterRecommendListOrder(Integer recommendedFileId, Integer newPosition) {
        try {
            List<RecommendedFile> presentList = recommendListMapper.getRecommendList();
            if(newPosition > presentList.size() || newPosition <= 0) {
                throw new APIException("非法位置");
            }
            int presentPosition = recommendListMapper.getPosition(recommendedFileId);
            if(presentPosition == newPosition) {
                return true;
            }
            recommendListMapper.alterRecommendedFilePosition(newPosition, recommendedFileId);
            //待调整的文件变动后其他文件自动向前或向后补位
            if(presentPosition > newPosition) {
                for(RecommendedFile tmp : presentList) {
                    int tmpPosition = tmp.getPosition();
                    if(tmpPosition >= newPosition && tmpPosition < presentPosition && tmp.getRecommendedFileId() != recommendedFileId) {
                        recommendListMapper.alterRecommendedFilePosition(tmpPosition + 1, tmp.getRecommendedFileId());
                    }
                }
            } else {
                for(RecommendedFile tmp : presentList) {
                    int tmpPosition = tmp.getPosition();
                    if(tmpPosition > presentPosition && tmpPosition <= newPosition && tmp.getRecommendedFileId() != recommendedFileId) {
                        recommendListMapper.alterRecommendedFilePosition(tmpPosition - 1, tmp.getRecommendedFileId());
                    }
                }
            }
            return true;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("添加文件错误");
        }
    }

    /**
     * 修改推荐列表中的文件
     *
     * @param recommendFileId 推荐列表中的文件对应推荐列表中的id
     * @param newId           新的文件在资源库中的id
     * @return boolean
     */
    @Override
    public boolean alterRecommendFile(Integer recommendFileId, Integer newId) {
        try {
            recommendListMapper.alterRecommendFile(newId, recommendFileId);
            return true;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("修改推荐列表错误");
        }
    }

    /**
     * 删除推荐列表中的文件
     *
     * @param recommendFileId 文件在推荐列表中对应的id
     * @return boolean
     */
    @Override
    public boolean deleteRecommendFile(Integer recommendFileId) {
        try {
            recommendListMapper.deleteRecommendFile(recommendFileId);
            return true;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("删除推荐列表文件错误");
        }
    }
}
