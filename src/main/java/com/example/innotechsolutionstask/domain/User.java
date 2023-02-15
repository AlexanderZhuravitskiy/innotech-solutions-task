package com.example.innotechsolutionstask.domain;

import com.example.innotechsolutionstask.web.constant.WebConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    @NotBlank(message = WebConstant.BLANK_FIELD_MESSAGE)
    private String username;

    @Column(name = "password")
    @NotBlank(message = WebConstant.BLANK_FIELD_MESSAGE)
    private String password;

    @Column(name = "active")
    private boolean active;

    @Column(name = "balance")
    private Integer balance;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_trains",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "trains_id")
    )
    private List<Train> trains = new ArrayList<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

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
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }
}