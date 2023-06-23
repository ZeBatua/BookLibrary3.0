package crud.app.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LibraryController {
    @GetMapping("/library")
    public String mainPage(Model model) {
        System.out.println("hello1");
        model.addAttribute("myString", "123");
        return "/library/mainPage";
    }
}
