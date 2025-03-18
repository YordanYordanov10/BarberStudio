package bg.softuni.barberstudio.User.Service;

import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.Web.Dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAdminInit implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAdminInit(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {

        Optional<User> adminInit = userService.findByUsername("admin123");

        if(adminInit.isPresent()) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin123");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin123@gmail.com");
        admin.setRole(UserRole.ADMIN);
        admin.setActive(true);

        userService.save(admin);
    }
}
