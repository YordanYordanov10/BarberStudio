package bg.softuni.barberstudio.Comment;

import bg.softuni.barberstudio.Comment.Model.Comment;
import bg.softuni.barberstudio.Comment.Repository.CommentRepository;
import bg.softuni.barberstudio.Comment.Service.CommentService;
import bg.softuni.barberstudio.Contact.Model.Contact;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.CommentCreateRequest;
import bg.softuni.barberstudio.Web.Dto.ContactRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceUTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;



    @Test
    void CreateContactAndSave() {

        CommentCreateRequest commentCreateRequest = new CommentCreateRequest();
        commentCreateRequest.setComment("test comment");

        User user = User.builder()
                .id(UUID.randomUUID())
                .profilePicture("www.test.com")
                .build();

        User barber = User.builder()
                .id(UUID.randomUUID())
                .build();

        Comment mockComment = Comment.builder()
                .id(UUID.randomUUID())
                .comment(commentCreateRequest.getComment())
                .author(user)
                .barber(barber)
                .imageUrl(user.getProfilePicture())
                .build();

        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

        Comment createdComment = commentService.createNewComment(commentCreateRequest, user,barber);


        assertNotNull(createdComment);
        assertEquals(commentCreateRequest.getComment(), createdComment.getComment());
        assertEquals(mockComment.getAuthor(),user);
        assertEquals(mockComment.getBarber(),barber);
        assertEquals(mockComment.getImageUrl(),user.getProfilePicture());


        verify(commentRepository, times(1)).save(any(Comment.class));

    }

    @Test
    void givenExistingCommentInDatabase_whenGetAllComments_thenReturnThemAll(){

        List<Comment> comments = List.of(new Comment(),new Comment());
        when(commentRepository.findAll()).thenReturn(comments);

        List<Comment> allComments = commentService.getAllComments();

        assertEquals(allComments.size(), 2);
    }

    @Test
    void givenCommentByBarberInDatabase_whenFindByBarber_thenReturnThem(){

        User barber = User.builder()
                .id(UUID.randomUUID())
                .build();

        Comment mockCommentBarber = Comment.builder()
                .id(UUID.randomUUID())
                .barber(barber)
                .build();

        when(commentService.getAllCommentsForBarber(barber.getId())).thenReturn(List.of(mockCommentBarber));

        List<Comment> comments = commentService.getAllCommentsForBarber(barber.getId());

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(barber.getId(), comments.get(0).getBarber().getId());
    }


}
