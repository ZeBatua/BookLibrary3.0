package crud.app.controllers;

import crud.app.models.Member;
import crud.app.services.MemberService;
import crud.app.util.MemberValidator;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/library/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberValidator memberValidator;

    @Autowired
    public MemberController(MemberService memberService, MemberValidator memberValidator) {
        this.memberService = memberService;
        this.memberValidator = memberValidator;
    }

    @GetMapping()
    public String memberList(Model model) {
        model.addAttribute("memberList", memberService.findAll());
        return "library/member/list";
    }

    @GetMapping("/{id}")
    public String info(@PathVariable("id") int id, Model model) {
        model.addAttribute("member", memberService.findOne(id));
        model.addAttribute("books", memberService.getBooksByPersonId(id));
        return "library/member/info";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("member", memberService.findOne(id));
        return "library/member/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("member") @Valid Member member, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        member.setId(id);

        if (bindingResult.hasErrors())
            return "/library/member/edit";

        memberService.save(member);
        return "redirect:/library/members";
    }

    @GetMapping("/new")
    public String newMember(@ModelAttribute("member") Member member) {
        return "library/member/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("member") @Valid Member member,
                         BindingResult bindingResult) {
        memberValidator.validate(member, bindingResult);

        if (bindingResult.hasErrors())
            return "library/member/new";

        memberService.save(member);
        return "redirect:/library/members";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        memberService.delete(id);
        return "redirect:/library/members";
    }

}
