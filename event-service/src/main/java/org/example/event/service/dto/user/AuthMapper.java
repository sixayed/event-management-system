package org.example.event.service.dto.user;

import org.example.event.service.model.UserEntity;
import org.example.event.service.security.User;
import org.example.event.service.security.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthMapper {

    LoginDto registerToLoginDto(RegisterDto registerDto);

    @Mapping(target = "authorities", expression = "java(mapToGrantedAuthorities(userEntity.getRole()))")
    User entityToUserDetails(UserEntity userEntity);

    default Collection<? extends GrantedAuthority> mapToGrantedAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
