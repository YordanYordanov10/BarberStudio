package bg.softuni.barberstudio.User.Service;

import bg.softuni.barberstudio.Exception.DomainException;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.User.Repository.UserRepository;
import bg.softuni.barberstudio.Web.Dto.RegisterRequest;
import bg.softuni.barberstudio.Web.Dto.UserEditRequest;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
                .isActive(true)
                .role(UserRole.USER)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User with this username does not exist"));

        return new AuthenticationDetails(user.getId(),username,user.getPassword(),user.getRole(),user.isActive);
    }

    public User getById(UUID id) {

        return userRepository.findById(id).orElseThrow(() -> new DomainException("User with this username does not exist"));
    }

    public void editUserDetails(UUID id, UserEditRequest userEditRequest) {

        User user = getById(id);

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setProfilePicture(userEditRequest.getProfilePictureUrl());

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @CacheEvict(value = "users", allEntries = true)
    public void updateUserRole(UUID userId, String role) {

       User user = getById(userId);
       user.setRole(UserRole.valueOf(role));
       userRepository.save(user);

    }

    @CacheEvict(value = "users", allEntries = true)
    public void updateUserStatus(UUID userId) {

       User user = getById(userId);
       user.setActive(!user.isActive());
       userRepository.save(user);
    }

    public List<User> findByUserRole() {

        return  userRepository.findByRole(UserRole.BARBER);
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}





