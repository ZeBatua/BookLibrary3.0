package crud.app.controllers.admin;

import crud.app.models.Member;
import crud.app.services.MemberService;
import crud.app.util.MemberValidator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/library/members")
@PreAuthorize("hasRole('ROLE_ADMIN')")
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
        return "library/admin/member/list";
    }

    @GetMapping("/{id}")
    public String info(@PathVariable("id") int id, Model model) {
        model.addAttribute("member", memberService.findOne(id));
        model.addAttribute("books", memberService.getBooksByPersonId(id));
        return "library/admin/member/info";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("member", memberService.findOne(id));
        return "library/admin/member/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("member") @Valid Member member, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        Member otherMemberData = memberService.findOne(id);

        member.setId(id);
        member.setRole(otherMemberData.getRole());
        member.setPassword(otherMemberData.getPassword());

        if (bindingResult.hasErrors())
            return "/library/admin/member/edit";
        System.out.println("hello3");


        memberService.save(member);

        return "redirect:/library/members";
    }

    @GetMapping("/new")
    public String newMember(@ModelAttribute("member") Member member) {
        return "library/admin/member/new";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        memberService.delete(id);
        return "redirect:/library/members";
    }

}
