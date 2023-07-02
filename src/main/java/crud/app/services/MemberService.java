package crud.app.services;

import crud.app.models.Book;
import crud.app.models.Member;
import crud.app.repositories.MemberRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll(Sort.by("name"));
    }

    public Member findOne(int id) {
        Optional<Member> foundMember = memberRepository.findById(id);
        return foundMember.orElse(null);
    }

    public Member findByName(String name) {
        Optional<Member> foundMember = memberRepository.findByName(name);
        return foundMember.orElse(null);

    }


    public Member findByEmail(String email) {
        Optional<Member> foundMember = memberRepository.findByEmail(email);
        return foundMember.orElse(null);
    }

    @Transactional
    public void save(Member person) {
        memberRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        memberRepository.deleteById(id);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Member> person = memberRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());

            // Проверка просроченности книг
            person.get().getBooks().forEach(book -> {
                long diffInMillis = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                // 864000000 миллисекунд = 10 суток
                if (diffInMillis > 864000000)
                    book.setExpired(true); // книга просрочена
            });

            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }
}
