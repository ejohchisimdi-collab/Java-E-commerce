package com.chisimdi.Ecommerce.user.mappers;

import com.chisimdi.Ecommerce.user.models.UserDTO;
import com.chisimdi.Ecommerce.user.models.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(Users users);
}
