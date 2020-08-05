package com.filmi3k.movies.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_info")
@NoArgsConstructor
@Getter
@Setter
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_info_id", nullable = false, unique = true, updatable = false)
    private int userInfoId;

    @Column(name = "first_name", length = 15)
    private String firstName;

    @Column(name = "last_name", length = 15)
    private String lastName;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, fetch =  FetchType.LAZY, orphanRemoval = true)
    private List<UserImage> userImages = new ArrayList<>();

    @ManyToOne(targetEntity = Gender.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "gender", nullable = true)
    private Gender gender;

    @JsonIgnore
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false, unique = true, updatable = false)
    private User user;

    public UserInfo(User user) { this.user = user; }
}
