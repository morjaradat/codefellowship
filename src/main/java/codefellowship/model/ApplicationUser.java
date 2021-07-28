package codefellowship.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ApplicationUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String password;

    @Column(unique = true)
    private String username;

    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String bio;

    @OneToMany(mappedBy = "applicationUser")
    private List<Post> posts;


    @ManyToMany(mappedBy = "following")
    Set<ApplicationUser> follower = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "followers",
            joinColumns = { @JoinColumn(name = "followerId")  },
            inverseJoinColumns ={  @JoinColumn(name = "followingId")  }
    )
    Set<ApplicationUser> following = new HashSet<>();



    public ApplicationUser( String username,String password, String firstName,
                           String lastName, Date dateOfBirth, String bio)
    {
        this.password = password;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
    }


    public Set<ApplicationUser> getFollower() {
        return follower;
    }

    public Set<ApplicationUser> getFollowing() {
        return following;
    }

    public ApplicationUser() {
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
