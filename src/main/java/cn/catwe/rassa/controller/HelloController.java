package cn.catwe.rassa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Creat by Catwe
 */
@Controller
public class HelloController {
    @GetMapping("/hellow")
    public String hellow(@RequestParam(name ="name") String name, Model model){
        model.addAttribute("name",name);
        return "hellow";
    }
}
