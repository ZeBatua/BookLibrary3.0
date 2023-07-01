package crud.app.controllers;

import crud.app.models.Book;
import crud.app.models.Member;
import crud.app.services.BookService;
import crud.app.services.MemberService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/library/books")
public class BookController {

    private final BookService bookService;
    private final MemberService memberService;

    @Autowired
    public BookController(BookService bookService, MemberService memberService) {
        this.bookService = bookService;
        this.memberService = memberService;
    }

    @GetMapping()
    public String bookList(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
        System.out.println("hello3");

        if (page == null || booksPerPage == null)
            model.addAttribute("bookList", bookService.findAll(sortByYear)); // выдача всех книг
        else
            model.addAttribute("bookList", bookService.findWithPagination(page, booksPerPage, sortByYear));

        return "library/book/list";
    }

    @GetMapping("/{id}")
    public String info(@PathVariable("id") int id, Model model, @ModelAttribute("member") Member member) {
        model.addAttribute("book", bookService.findById(id));

        Member bookOwner = bookService.getBookOwner(id);
        System.out.println(bookOwner);

        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("memberList", memberService.findAll());

        return "library/book/info";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findById(id));
        return "library/book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        book.setId(id);

        if (bindingResult.hasErrors())
            return "/library/book/edit";

        bookService.save(book);
        return "redirect:/library/books";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "library/book/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/library/book/new";

        bookService.save(book);
        return "redirect:/library/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/library/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/library/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("member") Member selectedMember) {
        bookService.assign(id, selectedMember);
        return "redirect:/library/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "/library/book/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", bookService.searchByName(query));
        return "library/book/search";
    }

}
