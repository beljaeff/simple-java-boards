package com.github.beljaeff.sjb.model;

import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

import static com.github.beljaeff.sjb.util.DateTimeUtils.DATE_TIME_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "private_messages")
public class PrivateMessage extends IdentifiedActiveEntity {

    @ManyToOne
    @JoinColumn(name = "id_parent", updatable = false)
    private PrivateMessage parentMessage;

    @OneToMany(mappedBy="parentMessage")
    private Set<PrivateMessage> childMessages;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_sender", nullable = false, updatable = false)
    private User sender;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_recipient", nullable = false, updatable = false)
    private User recipient;

    @Column(name = "is_seen", nullable = false)
    private boolean isSeen;

    @NotBlank
    @Size(min = 7, max = 15)
    @Column(name = "ip_create", nullable = false, updatable = false)
    @Pattern(regexp = "^(([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    private String ipCreate;

    @NotNull
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    @Column(name = "date_create", nullable = false, updatable = false)
    private LocalDateTime dateCreate;

    @NotBlank
    @Size(min = 2, max = 128)
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Size(max = 512)
    @Column(updatable = false)
    private String body;

    @ManyToMany
    @JoinTable(name = "private_message_attachments",
        joinColumns = @JoinColumn(name = "message_id"),
        inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private Set<Attachment> attachments;
}