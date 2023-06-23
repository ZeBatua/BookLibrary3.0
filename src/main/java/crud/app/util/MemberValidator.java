package crud.app.util;

import crud.app.dao.MemberDAO;
import crud.app.models.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// Класс задача которого проверить пользователя с таким же ФИО
@Component
public class MemberValidator implements Validator {

    private final MemberDAO memberDAO;

    @Autowired
    public MemberValidator(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Member member = (Member) target;

//        if(memberDAO.getPersonByName(member.getName()).isPresent())
//            errors.rejectValue("name", "", "Уже есть человек с таким ФИО");
    }
}
