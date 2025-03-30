package bg.softuni.barberstudio.web;

import bg.softuni.barberstudio.Comment.Model.Comment;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Comment.Service.CommentService;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.IndexController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    private User mockUser1;
    private User mockUser2;
    private User mockAuthUser;
    private Comment comment;
    private List<User> mockBarbers;
    private List<Comment> mockTestimonials;

    @BeforeEach
    void setUp() {
        UUID authenticatedUserId = UUID.randomUUID();
        mockUser1 = User.builder()
                .id(UUID.randomUUID())
                .username("daka123")
                .email("daka@gmail.com")
                .password("daka123")
                .build();

        mockUser2 = User.builder()
                .id(UUID.randomUUID())
                .username("daka123")
                .email("daka@gmail.com")
                .password("daka123")
                .build();

        comment = Comment.builder()
                .id(UUID.randomUUID())
                .barber(mockUser1)
                .author(mockUser2)
                .comment("lorem ipsum")
                .build();

        mockAuthUser = User.builder()
                .id(authenticatedUserId)
                .username("authenticatedUser")
                .email("auth@example.com")
                .password("password123")
                .build();

        mockBarbers = List.of(mockUser1, mockUser2);
        mockTestimonials = List.of(comment);
    }

    @Test
    void indexPageShouldReturnIndexViewWithBarbersAndTestimonialsWhenNotAuthenticated() throws Exception {


        when(userService.findByUserRole()).thenReturn(mockBarbers);
        when(commentService.getAllComments()).thenReturn(mockTestimonials);


        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("barbers", mockBarbers))
                .andExpect(model().attribute("testimonials", mockTestimonials))
                .andExpect(model().attribute("isLoggedIn", false));

        verify(userService, times(1)).findByUserRole();
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    void indexPageShouldReturnIndexViewWithUserDetailsWhenAuthenticated() throws Exception {
        UUID authenticatedUserId = mockAuthUser.getId();

        when(userService.findByUserRole()).thenReturn(mockBarbers);
        when(commentService.getAllComments()).thenReturn(mockTestimonials);
        when(userService.getById(authenticatedUserId)).thenReturn(mockAuthUser);


        AuthenticationDetails authDetails = new AuthenticationDetails(
                authenticatedUserId,
                mockAuthUser.getUsername(),
                mockAuthUser.getEmail(),
                mockAuthUser.getRole(),
                mockAuthUser.isActive
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(authDetails, null));
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get("/")
                        .with(request -> {
                            request.setUserPrincipal(securityContext.getAuthentication());
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(model().attribute("barbers", mockBarbers))
                .andExpect(model().attribute("testimonials", mockTestimonials))
                .andExpect(model().attribute("isLoggedIn", true))
                .andExpect(model().attribute("user", mockAuthUser));

        verify(userService,times(1)).findByUserRole();
        verify(commentService,times(1)).getAllComments();
        verify(userService, times(1)).getById(authenticatedUserId);
    }

    @Test
    void loginPageShouldReturnLoginViewWithLoginRequestDto() throws Exception {


        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"));

    }

    @Test
    void registerPageShouldReturnRegisterWithRegisterRequest() throws Exception {

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"));
    }


    @Test
    void registerUserHappyPath() throws Exception {

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "daka123")
                .formField("password", "daka123")
                .formField("confirmPassword", "daka123")
                .formField("email", "daka@gmail.com")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

                verify(userService,times(1)).register(any());
    }


    @Test
    void registerUserWithInvalidData() throws Exception {

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "daka")
                .formField("password", "daka")
                .formField("confirmPassword", "daka123")
                .formField("email", "daka@gmail.com")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registerRequest"));

                verify(userService,never()).register(any());
    }

    @Test
    void registerWithWrongPassword() throws Exception {

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "daka123")
                .formField("password", "daka123")
                .formField("confirmPassword", "daka456")
                .formField("email", "daka@gmail.com")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors(
                        "registerRequest", "confirmPassword"))
                .andExpect(model().attributeExists("registerRequest"));
    }


}
