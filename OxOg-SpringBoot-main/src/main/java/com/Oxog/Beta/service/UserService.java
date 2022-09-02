package com.Oxog.Beta.service;

import com.Oxog.Beta.model.Role;
import com.Oxog.Beta.model.User;
import com.Oxog.Beta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword()); // 패스워드 암호화
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        Role role = new Role();

        role.setId(1l);
        user.getRoles().add(role);

        return userRepository.save(user);
    }
}
