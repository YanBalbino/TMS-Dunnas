package com.dunnas.tms.feature.ticket_type;


import java.util.HashSet;
import java.util.Set;

import com.dunnas.tms.feature.base.BaseEntity;
import com.dunnas.tms.feature.user.UserAccount;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "ticket_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString
public class TicketType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "deadline_days", nullable = false)
    private Integer deadlineDays;

    // rule for collaborator assignment based on ticket type
    // ficar de olho quanto a comparações usando equals e hash code
    @Builder.Default
    @ToString.Exclude
    @ManyToMany(mappedBy = "collaboratorTicketTypes", fetch = FetchType.LAZY)
    private Set<UserAccount> collaborators = new HashSet<>();
}