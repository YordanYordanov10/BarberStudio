package bg.softuni.barberstudio.Web;

import bg.softuni.barberstudio.Comment.Service.CommentService;
import bg.softuni.barberstudio.Security.AuthenticationDetails;
import bg.softuni.barberstudio.User.Model.User;
import bg.softuni.barberstudio.User.Service.UserService;
import bg.softuni.barberstudio.Web.Dto.CommentCreateRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class CommentController {


    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }


    @PostMapping("/barber/{id}/comment")
    public String addNewComment(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @PathVariable("id") UUID id, @Valid CommentCreateRequest commentCreateRequest, BindingResult bindingResult) {

        User user = userService.getById(authenticationDetails.getId());
        User barber = userService.getById(id);

        if(bindingResult.hasErrors()) {
            return "redirect:/barber/{id}";
        }

        commentService.createNewComment(commentCreateRequest,user, barber);

        return "redirect:/barber/{id}";
    }


}
