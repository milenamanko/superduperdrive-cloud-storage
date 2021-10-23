package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (firstname, lastname, username, password)" +
            "VALUES #{firstName}, #{lastName}, #{username}, #{password}")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

}
