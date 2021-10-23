package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (firstname, lastname, username, password, salt)" +
            "VALUES #{firstName}, #{lastName}, #{username}, #{password}, #{salt}")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insertUser(User user);

    @Update("UPDATE USERS SET firstname = #{firstName}, lastname = #{lastName}, username = #{username}, password = #{password} WHERE userid = #{userId}")
    int updateUser(Integer userId);

    @Delete("DELETE FROM USERS WHERE userid = #{userId}")
    int deleteUser(Integer userId);

}
