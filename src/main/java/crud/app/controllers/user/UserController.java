package crud.app.controllers.user;

import crud.app.models.Member;
import crud.app.security.MemberDetails;
import crud.app.services.BookService;
import crud.app.services.MemberService;
import crud.app.util.MemberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_USER')" + " || hasRole('ADMIN')")
public class UserController {

    private final BookService bookService;
    private final MemberService memberService;

    @Autowired
    public UserController(BookService bookService, MemberService memberService) {
        this.bookService = bookService;
        this.memberService = memberService;
    }

    @GetMapping()
    public String userPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails personDetails = (MemberDetails) authentication.getPrincipal();
        Member currentMember = personDetails.getMember();

        model.addAttribute("memberInfo", memberService.findOne(currentMember.getId()));
        model.addAttribute("takenBooksList", memberService.getBooksByPersonId(currentMember.getId()));
        model.addAttribute("freeBooks", bookService.findFreeBooks());

        return "library/user/personalAccount";

    }

}
