package com.project.shopapp.services;

import com.project.shopapp.dtos.UserDto;
import com.project.shopapp.models.User;

public interface IUserService {
    User createUser(UserDto userDto);

    String login(String phoneNumber, String password);
}
