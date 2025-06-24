package com.kairgaliyev.backendonlineshop.entity.mapper;

import com.kairgaliyev.backendonlineshop.dto.UserDto;
import com.kairgaliyev.backendonlineshop.dto.UserRequest;
import com.kairgaliyev.backendonlineshop.entity.UserEntity;
import com.kairgaliyev.backendonlineshop.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
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
    @Mapping(target = "role", expression = "java(com.kairgaliyev.backendonlineshop.enums.UserRole.USER)")
    @Mapping(target = "id", ignore = true)
    public abstract UserEntity toNewUserEntity(UserRequest request);

    public abstract UserRequest toUserRequest(UserEntity entity);

    public abstract UserDto toUserDto(UserEntity entity);

//    TODO: fix java: Can't generate mapping method with no input arguments.
//    @Mapping(target = "name", source = "context", qualifiedByName = "generateUUID")
//    @Mapping(target = "role", source = "context", qualifiedByName = "unauthorizedRole")
//    public abstract UserEntity toUnauthorizedUserEntity(@Context UserContextMapper context);

    @Named("generateUUID")
    protected String userUuid(UserContextMapper context) {
        return context.generateRandomName();
    }

    @Named("unauthorizedRole")
    protected UserRole defaultUnauthorizedRole(UserContextMapper context) {
        return context.getDefaultUnauthorizedRole();
    }
}
