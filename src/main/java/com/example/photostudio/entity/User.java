package com.example.photostudio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(length = 65) // for storing encoded bcrypt password
    @JsonIgnore  // to not include in json response
    private String password;

    private String name;

    private String email;

    //default fetch type for OneToMany is LAZY

//    mappedBy is used on the inverse (non-owning) side.
//    It refers to the field name on the owning side that defines the relationship.
//    It prevents JPA from trying to create a redundant foreign key.
    // **if we dont use mappedBy so new redundant table with user and album id will be created
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore  // so that infinite nested fetching is not there in api response // either we can doesn't show list in api response or show list but we have to put then JsonIgnore at album side so that album doesn't fetch user
    private List<Album> albums;

    public void removeAlbum(Album album){
        albums.remove(album);
        album.setUser(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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
