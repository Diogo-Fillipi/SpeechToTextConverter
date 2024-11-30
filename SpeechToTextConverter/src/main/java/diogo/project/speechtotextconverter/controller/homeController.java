package diogo.project.speechtotextconverter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homeController {

    @GetMapping("/")
    public String homePage(){
        return "index.html";
    }

}
