package com.github.beljaeff.sjb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.beljaeff.sjb.model.common.NamedEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import com.github.beljaeff.sjb.enums.Gender;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static com.github.beljaeff.sjb.util.DateTimeUtils.DATE_PATTERN;
import static com.github.beljaeff.sjb.util.DateTimeUtils.DATE_TIME_PATTERN;

/*
 * Set fields for displaying user list
 */
@NamedEntityGraph(name = EntityGraphNamesHelper.USERS_WITH_AVATAR,
        attributeNodes = {
                @NamedAttributeNode(value = "avatar")
        }
)

/*
 * Set fields for displaying user profile overview and user profile groups
 */
@NamedEntityGraph(name = EntityGraphNamesHelper.USERS_WITH_AVATAR_AND_GROUPS,
        attributeNodes = {
                @NamedAttributeNode(value = "avatar"),
                @NamedAttributeNode(value = "groups")
        }
)

/*
 * Set fields for displaying user profile messages
 */
@NamedEntityGraph(name = EntityGraphNamesHelper.USERS_WITH_AVATAR_AND_GROUPS_AND_POSTS,
        attributeNodes = {
                @NamedAttributeNode(value = "avatar"),
                @NamedAttributeNode(value = "groups"),
                @NamedAttributeNode(value = "posts")
        }
)

/*
 * Set fields for authorizing user
 */
@NamedEntityGraph(name = EntityGraphNamesHelper.USERS_WITH_GROUPS_WITH_PERMISSIONS,
    attributeNodes = {
        @NamedAttributeNode(value = "avatar"),
        @NamedAttributeNode(value = "groups", subgraph = "groupWith.permissions")
    },
    subgraphs = {
        @NamedSubgraph(
                name = "groupWith.permissions",
                attributeNodes = @NamedAttributeNode(value = "permissions")
        )
    }
)

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User extends NamedEntity {

    @NotBlank
    @Pattern(regexp = "^[_a-zA-Z0-9-.]{3,}$")
    @Size(min = 3, max = 64)
    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Email(regexp = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    @NotBlank
    @Size(max = 64)
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank
    @Size(min = 5, max = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @NotNull
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    @Column(name = "date_registered", nullable = false, updatable = false)
    private LocalDateTime registeredDate;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    @Column(name = "date_last_login", nullable = false)
    private LocalDateTime lastLoginDate;

    @PositiveOrZero
    @Column(name = "posts_count", nullable = false)
    private int postsCount;

    private Gender gender;

    @Size(max = 128)
    private String surname;

    @DateTimeFormat(pattern = DATE_PATTERN)
    @Column(name = "date_birth")
    private LocalDate birthDate;

    @Size(max = 128)
    private String location;

    @Size(max = 128)
    private String site;

    @Column(name = "is_hide_email", nullable = false)
    private boolean hideEmail;

    @Column(name = "is_hide_date_birth", nullable = false)
    private boolean hideBirthdate;

    @Column(name = "is_show_online", nullable = false)
    private boolean showOnline;

    @Size(max = 32)
    private String signature;

    @OneToOne(fetch = FetchType.LAZY, cascade = { PERSIST, MERGE, REFRESH, DETACH })
    @JoinColumn(name = "avatar")
    private Attachment avatar;

    @PositiveOrZero
    @Column(name = "karma_bad", nullable = false)
    private int badKarma;

    @PositiveOrZero
    @Column(name = "karma_good", nullable = false)
    private int goodKarma;

    @NotNull
    @Size(max = 128)
    @Column(name = "secret_question", nullable = false)
    private String secretQuestion;

    @NotNull
    @Size(max = 128)
    @Column(name = "secret_answer", nullable = false)
    private String secretAnswer;

    @PositiveOrZero
    @Column(name = "secret_answer_tries", nullable = false)
    private int secretAnswerTries;

    @Column(name = "is_banned", nullable = false)
    private boolean isBanned;

    @Column(name = "is_activated", nullable = false)
    private boolean isActivated;

    @NotNull
    @Size(max = 128)
    @Column(name = "validation_code", nullable = false)
    private String validationCode;

    @NotNull
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    @Column(name = "last_validation_request_date", nullable = false)
    private LocalDateTime lastValidationRequestDate;

    @Column(name = "time_logged_in", nullable = false)
    private long timeLoggedIn;

    @OneToMany(mappedBy = "author")
    private Set<Topic> topics;

    @OneToMany(mappedBy = "author")
    private Set<Post> posts;

    @OneToMany(mappedBy = "recipient")
    private Set<PrivateMessage> privateMessagesIn;

    @OneToMany(mappedBy = "sender")
    private Set<PrivateMessage> privateMessagesOut;

    @OrderBy("weight ASC, name ASC")
    @ManyToMany
    @JoinTable(name = "user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> groups;

    @ManyToMany
    @JoinTable(name = "board_moderators",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "board_id")
    )
    private Set<Board> moderateBoards;
}