package com.project.shopapp.services.impl;

import com.project.shopapp.jwt.JwtTokenUtil;
import com.project.shopapp.dtos.UserDto;
import com.project.shopapp.exception.PhoneNumberAlreadyExistsException;
import com.project.shopapp.exception.ResourceNotFoundException;
import com.project.shopapp.exception.UserException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDto userDto) {
        String phoneNumber = userDto.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new PhoneNumberAlreadyExistsException("Phone number has already exists");
        }

        if(!userDto.getPassword().equals(userDto.getRetypePassword())){
            throw new UserException("Password does not match");
        }

        User newUser = User.builder()
                .fullName(userDto.getFullName())
                .phoneNumber(userDto.getPhoneNumber())
                .password(userDto.getPassword())
                .address(userDto.getAddress())
                .dateOfBirth(userDto.getDateOfBirth())
                .active(true)
                .facebookAccountId(userDto.getFacebookAccountId())
                .googleAccountId(userDto.getGoogleAccountId())
                .build();
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_USER");
        roles.add(role);
        newUser.setRoles(roles);
        if(userDto.getFacebookAccountId() == 0 && userDto.getGoogleAccountId() == 0){
            String password = userDto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }


        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", phoneNumber));

        if(user.getFacebookAccountId() == 0 && user.getGoogleAccountId() == 0){
           if(!passwordEncoder.matches(password,user.getPassword())){
               throw new BadCredentialsException("Wrong phone number or password");
           }
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(phoneNumber, password);
        String token = jwtTokenUtil.generateToken(authentication);
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return token;
    }
}
