package codefellowship.controller;

import codefellowship.Repository.ApplicationUserRepository;
import codefellowship.model.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

import java.sql.Date;

@Controller
public class AppController {


    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/")
    public String homePage(Principal accountDetail, Model model){
        System.out.println("home1");
        if(accountDetail != null) {
            model.addAttribute("user", applicationUserRepository.findUserByUsername(accountDetail.getName()));
            System.out.println("home2");
            return "homePage";
        } else {
            System.out.println("home3");
            return "login";
        }

    }

    @GetMapping("/signup")
    public String signUpPage(){
        return "signup";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }


    @GetMapping("/user/{id}")
    public String getUser(@PathVariable Long id, Model model){
        ApplicationUser user = applicationUserRepository.getById(id);
        model.addAttribute("user", user);
        return "profile";
    }


    @PostMapping("/signup")
    public RedirectView signup(
            @RequestParam String username
            ,@RequestParam String password
            ,@RequestParam String firstname
            ,@RequestParam String lastname
            ,@RequestParam Date dateOfBirth
            ,@RequestParam String bio){
        ApplicationUser newUser = new ApplicationUser(username, encoder.encode(password),firstname,lastname,dateOfBirth,bio);

        newUser = applicationUserRepository.save(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, newUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        model.addAttribute("user",applicationUserRepository.findAll());

        return new RedirectView("/login");
    }
    @GetMapping("/access-denied")
    public String getAccessDenied() {
        return "/403";
    }

}
