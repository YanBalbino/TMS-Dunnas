package com.dunnas.tms.feature.block;

import java.util.ArrayList;
import java.util.List;

import com.dunnas.tms.feature.base.BaseEntity;
import com.dunnas.tms.feature.unit.Unit;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "block")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(exclude = "units")
public class Block extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Block name is required")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "number")
    private Integer number;

    @Column(name = "floor_count")
    private Integer floorCount;

    @Column(name = "units_per_floor")
    private Integer unitsPerFloor;

    @Builder.Default
    @OneToMany(mappedBy = "block", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Unit> units = new ArrayList<>();
}