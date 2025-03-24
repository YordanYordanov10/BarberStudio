package bg.softuni.barberstudio.Comment.Repository;

import bg.softuni.barberstudio.Comment.Model.Comment;
import bg.softuni.barberstudio.User.Model.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findAllByAuthor(User author);

    List<Comment> findByBarberId(UUID barberId);
}
