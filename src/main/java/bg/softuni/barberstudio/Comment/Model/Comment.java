package bg.softuni.barberstudio.Comment.Model;

import bg.softuni.barberstudio.User.Model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String comment;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "barber_id", referencedColumnName = "id", nullable = false)
    private User barber;

}
