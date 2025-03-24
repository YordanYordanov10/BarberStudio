package bg.softuni.barberstudio.Comment.Service;

import bg.softuni.barberstudio.Comment.Model.Comment;
import bg.softuni.barberstudio.Comment.Repository.CommentRepository;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.Web.Dto.CommentCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    public void createNewComment(CommentCreateRequest commentCreateRequest, User user, User barber) {

        Comment comment = Comment.builder()
                .comment(commentCreateRequest.getComment())
                .imageUrl(user.getProfilePicture())
                .author(user)
                .barber(barber)
                .build();

        commentRepository.save(comment);

    }

    public List<Comment> getAllComments() {

        return commentRepository.findAll();
    }

    public List<Comment> getAllCommentsForBarber(UUID barberId) {

        return commentRepository.findByBarberId(barberId);
    }
}
