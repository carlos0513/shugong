package com.frontend.modular.system.factory;

import com.frontend.modular.system.transfer.UserDto;
import com.frontend.modular.system.model.User;
import org.springframework.beans.BeanUtils;

/**
 * 用户创建工厂
 */
public class UserFactory {

    public static User createUser(UserDto userDto){
        if(userDto == null){
            return null;
        }else{
            User user = new User();
            BeanUtils.copyProperties(userDto,user);
            return user;
        }
    }
}
