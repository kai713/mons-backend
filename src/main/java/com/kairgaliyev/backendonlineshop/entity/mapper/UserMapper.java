package com.kairgaliyev.backendonlineshop.entity.mapper;

import com.kairgaliyev.backendonlineshop.dto.UserDto;
import com.kairgaliyev.backendonlineshop.dto.UserRequest;
import com.kairgaliyev.backendonlineshop.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Кодировщик пароля внедряется через Spring.
 * При маппинге пароль сразу шифруется.
 */
@Mapper(componentModel = "spring")   // генерируемый класс станет Spring-бином
public abstract class UserMapper {

    @Autowired                      // конструкторная инъекция в сгенерированном классе
    protected PasswordEncoder passwordEncoder;

    @Mapping(
            target = "password",
            expression = "java(passwordEncoder.encode(request.password()))"
    )
    @Mapping(target = "role", defaultValue = "USER")
    public abstract UserEntity toNewUserEntity(UserRequest request);

    public abstract UserRequest toUserRequest(UserEntity entity);

    public abstract UserDto toUserDto(UserEntity entity);
}
