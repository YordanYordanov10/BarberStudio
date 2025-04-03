package bg.softuni.barberstudio.web;

import bg.softuni.barberstudio.Comment.Service.CommentService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Model.UserRole;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.CommentController;
import bg.softuni.barberstudio.Web.Dto.CommentCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class CommentControllerApiTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private AuthenticationDetails authenticationDetails;

    private AuthenticationDetails principal;
    private User mockUser;
    private User mockBarber;

    @BeforeEach
    void setUp() {

        principal = AuthenticationDetails.builder()
                .id(UUID.randomUUID())
                .username("daka123")
                .password("daka123")
                .isActive(true)
                .role(UserRole.ADMIN)
                .build();

        mockBarber = User.builder()
                .id(UUID.randomUUID())
                .username("daka123")
                .role(UserRole.BARBER)
                .email("daka@gmail.com")
                .password("daka123")
                .build();

        mockUser = User.builder()
                .id(UUID.randomUUID())
                .username("daka123")
                .role(UserRole.BARBER)
                .email("daka@gmail.com")
                .password("daka123")
                .build();

    }

    @Test
    void addNewComment_ShouldRedirectToBarber_WhenCommentIsValid() throws Exception {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken(principal, null,"ROLE_USER"));
        SecurityContextHolder.setContext(securityContext);

        UUID userId = principal.getId();
        UUID barberId = mockBarber.getId();

        CommentCreateRequest comment = new CommentCreateRequest();
        comment.setComment("test testt");

        commentService.createNewComment(comment,mockUser,mockBarber);


        mockMvc.perform(post("/barber/{id}/comment", barberId)
                        .param("comment", comment.getComment())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/barber/{id}"));

        verify(commentService, times(1)).createNewComment(comment,mockUser,mockBarber);
    }

}
