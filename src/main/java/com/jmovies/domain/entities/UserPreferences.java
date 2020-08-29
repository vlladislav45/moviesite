package com.jmovies.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static com.jmovies.config.Config.BASE_THEME;

@Entity
@Table(name = "user_preferences")
@NoArgsConstructor
@Getter
@Setter
public class UserPreferences {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_preference_id", nullable = false, unique = true, updatable = false)
    private int userPreferenceId;

    @Column(name = "selected_theme", length = 11, nullable = false)
    private String selectedTheme = BASE_THEME;

    @JsonIgnore
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false, unique = true, updatable = false)
    private User user;

    public UserPreferences(User user) {
        this.user = user;
    }

    public UserPreferences(User user, String selectedTheme) {
        this.user = user;
        this.selectedTheme = selectedTheme;
    }
}
