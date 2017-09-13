package examples.spring.restservices.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    private String username;
    private String password;

    @OneToMany(mappedBy = "account")
    private Set<Bookmark> bookmarks = new HashSet<>();


    public Account() { }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<Bookmark> getBookmarks() {
        return bookmarks;
    }

}
