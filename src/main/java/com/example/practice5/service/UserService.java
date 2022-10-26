package com.example.practice5.service;

import com.example.practice5.dto.IUserDTO;
import com.example.practice5.exception.PasswordCheckException;
import com.example.practice5.model.User;
import com.example.practice5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new UsernameNotFoundException("Пользователя с таким именем не существует");
        }
        return userRepository.findByUsername(username).get();
    }

    @Transactional
    public void create(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void update(User user) {
        userRepository.save(user);
    }

    public String checkDTO(IUserDTO dto) throws PasswordCheckException {
        if (dto.getPassword() != null) {
            String CHECK = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+)[a-zA-Z0-9_-]{8,}$";
            Pattern pattern = Pattern.compile(CHECK);
            Matcher matcher = pattern.matcher(dto.getPassword());
            if (!matcher.matches()) {
                throw new PasswordCheckException("Пароль должен содержать как миниму одну строчную букву, " +
                        "одну заглавную букву и одну цифру, а также быть не менне 8 символов в длину.");
            }
            return passwordEncoder.encode(dto.getPassword());
        }
        return null;
    }
}
