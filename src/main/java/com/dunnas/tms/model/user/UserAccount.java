package com.dunnas.tms.model.user;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.Hibernate;

import com.dunnas.tms.model.base.BaseEntity;
import com.dunnas.tms.model.ticket_type.TicketType;
import com.dunnas.tms.model.unit.Unit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Username is required")
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false, length = 100)
    @ToString.Exclude
    private String password;

    @NotBlank(message = "Role is required")
    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Builder.Default
    @ManyToMany(mappedBy = "residents", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Unit> units = new HashSet<>();

    // rule for collaborator assignment based on ticket type, using join table with user_id and ticket_type_id
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "collaborator_ticket_type",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "ticket_type_id")
    )
    @ToString.Exclude
    private Set<TicketType> collaboratorTicketTypes = new HashSet<>();

    // unique equals and hashCode methods based on id for proper entity comparison and hashing
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        UserAccount object = (UserAccount) o;
        return id != null && Objects.equals(id, object.id);
    }

    @Override
    public final int hashCode() {
        return id != null ? id.hashCode() : Hibernate.getClass(this).hashCode();
    }
}
