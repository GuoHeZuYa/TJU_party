package edu.twt.party.dao.homepage;

import edu.twt.party.pojo.homepage.Carousel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarouselMapper {
    @Insert("insert into `carousel` (`file_id`, `link`, `title`, `picture_path`, `position`) values(#{fileId}, #{link}, #{title}, #{picturePath}, #{position})")
    int addCarousel(@Param("fileId") int fileId, @Param("link") String link, @Param("title") String title, @Param("picturePath") String picturePath, @Param("position") int position);

    @Update("update `carousel` set is_deleted = 1 where carousel_id = #{carouselId}")
    int deleteCarousel(@Param("carouselId") int carouselId);

    @Update("update `carousel` set position = #{newPosition} where carousel_id = #{carouselId}")
    int alterCarouselPosition(@Param("newPosition") int newPosition, @Param("carouselId") int carouselId);

    @Update("update `carousel` set file_id = #{fileId}, link = #{link}, title = #{title}, picture_path = #{picturePath} where carousel_id = #{carouselId}")
    int alterCarousel(@Param("fileId")  int fileId, @Param("carouselId") int carouselId, @Param("link") String link, @Param("title") String title, @Param("picturePath") String picturePath);

    @Select("select * from `carousel` where is_deleted = 0 order by position")
    List<Carousel> getCarousel();

    @Select("select position from `carousel` where carousel_id = #{carouselId} and is_deleted = 0")
    int getPositionById(@Param("carouselId") int carouselId);
}
