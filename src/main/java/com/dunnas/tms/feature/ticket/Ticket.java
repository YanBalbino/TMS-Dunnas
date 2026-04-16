package com.dunnas.tms.feature.ticket;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dunnas.tms.feature.attachment.Attachment;
import com.dunnas.tms.feature.base.BaseEntity;
import com.dunnas.tms.feature.comment.Comment;
import com.dunnas.tms.feature.ticketStatus.TicketStatus;
import com.dunnas.tms.feature.ticketType.TicketType;
import com.dunnas.tms.feature.unit.Unit;
import com.dunnas.tms.feature.user.UserAccount;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Exclude 
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Size(min = 10, max = 300, message = "Description must be between 10 and 300 characters")
    @Column(name = "description", length = 300)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private TicketStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserAccount author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "due_date")
    private OffsetDateTime dueDate;

    @Builder.Default
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude private List<Attachment> attachments = new ArrayList<>();
}
