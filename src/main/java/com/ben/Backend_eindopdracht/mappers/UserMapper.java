package com.ben.Backend_eindopdracht.mappers;

import com.ben.Backend_eindopdracht.dtos.UserInputDto;
import com.ben.Backend_eindopdracht.dtos.UserOutputDto;
import com.ben.Backend_eindopdracht.models.User;
import java.util.List;
import java.util.stream.Collectors;


public class UserMapper {

    //van user naar OutputDto
    public static UserOutputDto toOutputDto(User user){
    var o = new UserOutputDto();
    o.setId(user.getId());
    o.setUsername(user.getUsername());
    o.setEmail(user.getEmail());
    /*o.setRole(user.getRole());*/
    return o;
}
    // list maken
    public static List<UserOutputDto> toOutputDtoList(List<User> users){
        return users.stream().map(UserMapper::toOutputDto).collect(Collectors.toList());

    }

    // van UserInputDTO naar User
    public static User toEntity (UserInputDto dto){
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        /*u.setRole(dto.getRole());*/
        return u;
    }
}
