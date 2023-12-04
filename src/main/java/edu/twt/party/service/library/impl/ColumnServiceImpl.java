package edu.twt.party.service.library.impl;

import edu.twt.party.dao.library.ColumnMapper;
import edu.twt.party.exception.APIException;
import edu.twt.party.pojo.library.Column;
import edu.twt.party.pojo.library.ColumnInfo;
import edu.twt.party.pojo.library.LibraryFile;
import edu.twt.party.service.homepage.NoticeService;
import edu.twt.party.service.library.ColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author xsr
 * 栏目及文章ServiceImpl
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class ColumnServiceImpl implements ColumnService {
    @Resource
    ColumnMapper columnMapper;

    @Override
    public boolean addNewColumn(String columnName, int position) {
        List<Column> originalColumns = columnMapper.getAllColumns();
        if(originalColumns.size() == 0){
            if(position != 1)
                throw new APIException("栏目位置错误");
            columnMapper.addColumn(columnName, position);
        }
        else{
            int maxPosition = columnMapper.getMaxColumnPosition();
            //若新增的栏目在尾部插入
            if (position > maxPosition) {
                if (position > maxPosition + 1)
                    throw new APIException("栏目位置错误");
                columnMapper.addColumn(columnName, position);
            }
            //若新增的栏目在中间插入
            else {
                for (Column i : originalColumns) {
                    if (i.getPosition() >= position) {
                        columnMapper.alterPosition(i.getPosition() + 1, i.getColumnId());
                    }
                }
                columnMapper.addColumn(columnName, position);
            }
        }
        return true;
    }

    @Override
    public boolean alterColumnName(String newName, int columnId) {
        Column temp = columnMapper.getColumnByColumnId(columnId);
        if (temp == null) throw new APIException("栏目不存在");
        columnMapper.alterColumnName(newName, columnId);
        return true;
    }

    @Override
    public boolean deleteColumn(int columnId) throws APIException {
        try {
            if(columnId == NoticeService.NOTICE_COLUMN_ID) {
                throw new APIException("不能删除固定的通知栏目");
            }
            int position = columnMapper.getColumnPosition(columnId);
            columnMapper.deleteColumn(columnId);
            List<Column> columns = columnMapper.getAllColumns();
            for (Column i : columns) {
                if (i.getPosition() > position) {
                    columnMapper.alterPosition(i.getPosition() - 1, i.getColumnId());
                }
            }
            return true;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("删除栏目错误");
        }
    }

    @Override
    public boolean alterColumnPosition(int columnId, int newPosition) throws APIException {
        try {
            if(columnId == NoticeService.NOTICE_COLUMN_ID) {
                throw new APIException("不能改变固定的通知栏目的位置");
            }
            Column presentColumn = columnMapper.getColumnByColumnId(columnId);
            if (presentColumn == null) throw new APIException("栏目不存在");
            int presentPosition = presentColumn.getPosition();
            int maxPosition = columnMapper.getMaxColumnPosition();
            if (newPosition > maxPosition) throw new APIException("栏目位置错误");
            if (newPosition == presentPosition) return true;
            List<Column> originalColumns = columnMapper.getAllColumns();
            if (newPosition < presentPosition) {
                for (Column i : originalColumns) {
                    if (i.getPosition() >= newPosition && i.getPosition() <= presentPosition && i.getColumnId() != columnId)
                        columnMapper.alterPosition(i.getColumnId(), i.getPosition() + 1);
                }
            } else {
                for (Column i : originalColumns) {
                    if (i.getPosition() <= newPosition && i.getPosition() >= presentPosition && i.getColumnId() != columnId)
                        columnMapper.alterPosition(i.getColumnId(), i.getPosition() - 1);
                }
            }
            columnMapper.alterPosition(columnId, newPosition);
            return true;
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException("删除栏目错误");
        }
    }

    @Override
    public List<ColumnInfo> getColumns() {
        try{
            return columnMapper.getAllColumnsWithNumber();
        } catch(Exception e) {
            e.printStackTrace();
            throw new APIException("获取栏目错误");
        }
    }

    @Override
    public List<LibraryFile> getFileOfColumn(int columnId) {
        List<LibraryFile> libraryFiles = columnMapper.getPassagesOfColumn(columnId);
        //根据发布时间进行排序
        libraryFiles.sort((LibraryFile o1, LibraryFile o2) -> {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date o1Date = ft.parse(o1.getCreatedTime());
                Date o2Date = ft.parse(o2.getCreatedTime());
                return o1Date.compareTo(o2Date);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new APIException("时间格式不正确");
            }
        });
        return libraryFiles;
    }

    @Override
    public boolean addFileToColumn(String title, String address, int columnId) {
        Column temp = columnMapper.getColumnByColumnId(columnId);
        if (temp == null) throw new APIException("栏目不存在");
        columnMapper.addFile(columnId, address, title);
        return true;
    }

    @Override
    public boolean deleteFileFromColumn(int passageId) {
        columnMapper.deleteFile(passageId);
        return true;
    }
}
