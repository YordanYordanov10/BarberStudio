package User;

import bg.softuni.barberstudio.Exception.DomainException;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Repository.UserRepository;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    void givenExistingUsernameOrEmail_whenRegister_thenExceptionIsThrown(){

        //Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("daka123")
                .password("daka123")
                .confirmPassword("daka123")
                .email("daka123@gmail.com")
                .build();
        when(userRepository.findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(DomainException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenHappyPath_whenRegister_thenUserIsSaved() {
        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("daka123")
                .password("daka123")
                .confirmPassword("daka123")
                .email("daka123@gmail.com")
                .build();

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password("hashed_password")
                .build();


        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User registerUser = userService.register(registerRequest);

        // Then
        assertNotNull(registerUser);
        assertEquals(savedUser.getId(), registerUser.getId());
        assertEquals(savedUser.getUsername(), registerUser.getUsername());
        assertEquals(savedUser.getEmail(), registerUser.getEmail());

        verify(userRepository, times(1)).findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
