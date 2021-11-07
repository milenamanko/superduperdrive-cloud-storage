package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FilesMapper {

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFile(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getAllFiles(Integer userId);

    @Select("SELECT COUNT(*) FROM FILES WHERE filename = #{filename}")
    int getFileCount(String filename);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata)" +
            "VALUES (#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Update("UPDATE FILES SET filename = #{filename} WHERE fileid = #{fileId}")
    int updateFileName(String filename, Integer fileId);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    int deleteFile(Integer fileId);
}
