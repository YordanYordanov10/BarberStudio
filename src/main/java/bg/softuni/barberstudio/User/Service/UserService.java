package bg.softuni.barberstudio.User.Service;

import bg.softuni.barberstudio.Exception.DomainException;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Repository.UserRepository;
import bg.softuni.barberstudio.Web.Dto.LoginRequest;
import bg.softuni.barberstudio.Web.Dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(LoginRequest loginRequest){

        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if(userOptional.isEmpty()){
            throw new DomainException("Username or Password is incorrect");
        }

        User user = userOptional.get();

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new DomainException("Username or Password is incorrect");
        }


        return user;
    }


    public User register(RegisterRequest registerRequest) {

        Optional<User> byUsernameOrEmail = userRepository.findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail());

        if(byUsernameOrEmail.isPresent()) {
            throw new DomainException("Username already exists");
        }

       return userRepository.save(initializeUser(registerRequest));

    }

    private User initializeUser(RegisterRequest registerRequest) {

        return User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
    }

}





