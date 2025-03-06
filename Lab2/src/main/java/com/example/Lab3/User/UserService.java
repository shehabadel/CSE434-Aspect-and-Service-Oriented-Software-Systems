package com.example.Lab3.User;

import com.example.Lab3.User.dto.CreateUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public class UserService {
    @Autowired
private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public List<User> getUsers(){
        return this.userRepository.findAll();
    }

    public User createUser( CreateUserDto userDto){
        User user = new User(
            userDto.getEmail(),
                userDto.getUsername(),
                userDto.getPhoneNumber(),
                userDto.getPassword()
        );
        return this.userRepository.save(user);
    }
}
