package bg.softuni.barberstudio.User;

import bg.softuni.barberstudio.Exception.DomainException;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.User.Repository.UserRepository;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.RegisterRequest;
import bg.softuni.barberstudio.Web.Dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;
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

    @Test
    void givenMissingUserFromDatabase_whenLoadUserByUsername(){

        String username = "daka123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void givenExistUser_whenLoadUserByUsername(){

        //Given
        String username = "daka123";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .password("hashed_password")
                .isActive(true)
                .role(UserRole.USER)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        //When
        UserDetails actualUserDetails = userService.loadUserByUsername(username);

        assertInstanceOf(AuthenticationDetails.class, actualUserDetails);
        AuthenticationDetails result = (AuthenticationDetails) actualUserDetails;
        assertNotNull(actualUserDetails);
        assertEquals(username, result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.isActive,result.isActive());
        assertEquals(user.getRole(),result.getRole());
    }

    @Test
    void givenUserById_whenGetById_ThrowException(){

        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userService.getById(id));
    }

    @Test
    void givenUserById_whenGetById(){

        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertNotNull(userService.getById(id));
        assertEquals(id, userService.getById(id).getId());
    }

    @Test
    void givenUserDetailEdit_whenUserEditDetails(){

        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .build();

        UserEditRequest userEditRequest = UserEditRequest.builder()
                .firstName("Daka")
                .lastName("dakov")
                .profilePictureUrl("www.daka.com")
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

       userService.editUserDetails(id, userEditRequest);

       assertEquals("Daka", user.getFirstName());
       assertEquals("dakov", user.getLastName());
       assertEquals("www.daka.com", user.getProfilePicture());
       verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenListOfUsers_whenGetAllUsers(){

        //Given
        List<User> userList = List.of(new User(),new User(),new User());

        //When
        when(userRepository.findAll()).thenReturn(userList);

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(3);
    }


    @Test
    void givenUsersWithBarberRole_shouldReturnList() {

        User barber = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.BARBER)
                .build();

        User barber1 = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.BARBER)
                .build();
        List<User> barbers = List.of(barber,barber1);

        when(userRepository.findByRole(UserRole.BARBER)).thenReturn(barbers);

        // Act
        List<User> result = userService.findByUserRole();

        // Assert
        assertEquals(2, result.size());
        assertEquals(barbers, result);
        verify(userRepository, times(1)).findByRole(UserRole.BARBER);
    }




}
