package com.spring.security.mapper;

import com.spring.security.dto.UserDTO;
import com.spring.security.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO dto);

    UserDTO toDTO(User user);
}
