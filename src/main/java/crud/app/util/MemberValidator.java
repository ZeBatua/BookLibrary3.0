package crud.app.util;

import crud.app.models.Member;
import crud.app.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// Класс, задача которого проверить пользователя с таким же ФИО
@Component
public class MemberValidator implements Validator {

    MemberService memberService;

    @Autowired
    public MemberValidator(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Member member = (Member) target;

        if (memberService.findByName(member.getName()) != null)
            errors.rejectValue("name", "", "Уже есть человек с таким ФИО");
    }
}
