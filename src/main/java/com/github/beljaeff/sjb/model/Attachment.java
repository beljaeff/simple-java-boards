package com.github.beljaeff.sjb.model;

import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import com.github.beljaeff.sjb.enums.AttachmentType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

import static com.github.beljaeff.sjb.util.DateTimeUtils.DATE_PATTERN;

//TODO: attachments removing from db have to be initiated from service layer together with removing attachment-files.
//TODO: it have to be done when delete board, topic or post
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "attachments")
public class Attachment extends IdentifiedActiveEntity {

    @NotBlank
    @Size(min = 3, max = 128)
    @Column(name = "original_name", nullable = false, updatable = false)
    private String originalName;

    @NotBlank
    @Size(min = 3, max = 128)
    @Column(name = "file_name", nullable = false, updatable = false)
    private String fileName;

    @NotNull
    @DateTimeFormat(pattern = DATE_PATTERN)
    @Column(name = "date_upload", nullable = false, insertable = false, updatable = false)
    private LocalDateTime dateUpload;

    @NotNull
    @Column(nullable = false, updatable = false)
    private AttachmentType type;

    @NotBlank
    @Column(name="content_type", nullable = false, updatable = false)
    private String contentType;

    @Getter
    @Setter
    @Size(max = 128)
    private String description;

    @OneToOne(mappedBy="avatar")
    private User owner;

    @ManyToMany
    @JoinTable(name = "post_attachments",
            joinColumns = @JoinColumn(name = "attachment_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> posts;

    @Override
    public String toString() {
        return "Attachment{" +
               ", id='" + getId() + '\'' +
               ", isActive='" + getIsActive() + '\'' +
               ", originalName='" + originalName + '\'' +
               ", fileName='" + fileName + '\'' +
               ", dateUpload=" + dateUpload +
               ", type=" + type +
               ", description='" + description + '\'' +
               '}';
    }
}