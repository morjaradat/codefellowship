package codefellowship.controller;

import codefellowship.Repository.ApplicationUserRepository;
import codefellowship.Repository.PostRepository;
import codefellowship.model.ApplicationUser;
import codefellowship.model.Post;
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
import java.util.List;

@Controller
public class AppController {


    @Autowired
    ApplicationUserRepository applicationUserRepository;
    @Autowired
    PostRepository postRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/")
    public String homePage(Principal accountDetail, Model model){
        if(accountDetail != null) {
            ApplicationUser user = applicationUserRepository.findUserByUsername(accountDetail.getName());
            model.addAttribute("user", user);
            List<ApplicationUser> followers = findAllExceptUserName(user);
            model.addAttribute("followers",followers);
        return "homePage";
        } else {
            return "login";
        }
    }
    public List<ApplicationUser> findAllExceptUserName(ApplicationUser userName){
        List<ApplicationUser> user = applicationUserRepository.findAll();
        user.remove(userName);
        return user;
    }

    @GetMapping("/myProfile")
    public String getProfile(Principal accountDetail, Model model){
        if (accountDetail != null){
            model.addAttribute("user", applicationUserRepository.findUserByUsername(accountDetail.getName()));
            return "profile";
        }else {
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
    public String getUser(@PathVariable Long id, Principal accountDetail,Model model){
        if(accountDetail != null) {
            ApplicationUser user = applicationUserRepository.getById(id);
            model.addAttribute("user", user);
            return "profile";
        }else {
            return "login";
        }

    }

    @PostMapping("/post/{id}")
    public RedirectView newPost(@PathVariable Long id ,@RequestParam String body ){
        ApplicationUser applicationUserId=applicationUserRepository.getById(id);
        Post post = new Post(body,applicationUserId);
        postRepository.save(post);
        return new RedirectView("/");
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

    @PostMapping("/follow")
    public RedirectView Follow(Principal principal,@RequestParam Long id){
        ApplicationUser currentUser = applicationUserRepository.findUserByUsername(principal.getName());
        ApplicationUser follower = applicationUserRepository.findUserById(id);
        currentUser.getFollowing().add(follower);
        applicationUserRepository.save(currentUser);

        return new RedirectView("/");
    }
    @PostMapping("/unfollow")
    public RedirectView unFollow(Principal principal,@RequestParam Long id){
        ApplicationUser currentUser = applicationUserRepository.findUserByUsername(principal.getName());
        ApplicationUser follower = applicationUserRepository.findUserById(id);
        currentUser.getFollowing().remove(follower);
        applicationUserRepository.save(currentUser);

        return new RedirectView("/");
    }

    @GetMapping("/feed")
    public String getFedd(Principal principal,Model model){
        model.addAttribute("user",applicationUserRepository.findUserByUsername(principal.getName()));
        return "feed";
    }

    @GetMapping("/access-denied")
    public String getAccessDenied() {
        return "403";
    }

}
