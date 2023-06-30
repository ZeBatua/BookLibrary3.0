package crud.app.controllers;

import crud.app.models.Member;
import crud.app.services.RegistrationService;
import crud.app.util.MemberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final MemberValidator memberValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(MemberValidator memberValidator, RegistrationService registrationService) {
        this.memberValidator = memberValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("member") Member member) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("member") @Valid Member member,
                                      BindingResult bindingResult) {
        memberValidator.validate(member, bindingResult);

        if (bindingResult.hasErrors())
            return "/auth/registration";

        registrationService.register(member);

        return "redirect:/auth/login";
    }


}
