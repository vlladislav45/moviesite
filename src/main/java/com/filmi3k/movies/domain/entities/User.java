package com.filmi3k.movies.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Immutable;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "user")
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private int userId;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "username", length = 20, nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnore
    @Column(name = "created_time", nullable = false)
    private Timestamp createdTime;

    @JsonIgnore
    @Column(name = "ip_address")
    private String ipAddress;

    @ManyToMany(targetEntity = UserRole.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<UserRole> authorities = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private UserPreferences userPreferences;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<UsersRating> usersRatings = new HashSet<>();

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Bookmark> bookMarks = new HashSet<>();

    @JsonIgnore
    private boolean isAccountNonExpired;

    @JsonIgnore
    private boolean isAccountNonLocked;

    @JsonIgnore
    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public List<UserRole> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(List<UserRole> authorities) {
        this.authorities = authorities;
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
        return isEnabled;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) { isCredentialsNonExpired = credentialsNonExpired; }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public UserPreferences getUserPreferences() { return userPreferences; }

    public void setUserPreferences(UserPreferences userPreferences) { this.userPreferences = userPreferences; }

    public UserInfo getUserInfo() { return userInfo; }

    public void setUserInfo(UserInfo userInfo) { this.userInfo = userInfo; }

    public Set<UsersRating> getUsersRatings() {
        return usersRatings;
    }

    public void setUsersRatings(Set<UsersRating> usersRatings) {
        this.usersRatings = usersRatings;
    }

    public Set<Bookmark> getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(Set<Bookmark> bookMarks) {
        this.bookMarks = bookMarks;
    }
}
