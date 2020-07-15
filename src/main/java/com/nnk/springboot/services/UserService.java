package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserFromAuth(Authentication auth) {
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails)auth.getPrincipal();
            User user = findUserByUsername(userDetails.getUsername());
            return user;
        }
        return null;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user;
        }
        return null;
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User createUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void updateUser(User user) {
        User updatedUser = findById(user.getId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        updatedUser.setPassword(encoder.encode(user.getPassword()));
        updatedUser.setFullname(user.getFullname());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setRole(user.getRole());
        userRepository.save(updatedUser);
    }

    public void deleteUser(Integer id) {
        if (userRepository.findById(id) != null) {
            userRepository.deleteById(id);
        }
    }
}
