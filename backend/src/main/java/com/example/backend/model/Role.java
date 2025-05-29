package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = @Index(name = "idx_admin", columnList = "admin"))
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition="boolean default false")
    private boolean admin;

    @Column(columnDefinition="boolean default false")
    private boolean read;

    @Column(columnDefinition="boolean default false")
    private boolean write;

    @Column(columnDefinition="boolean default false")
    private boolean delete;

    @JsonIgnore
    @OneToMany(mappedBy="role", orphanRemoval = true)
    private Set<UserDrawingRole> userDrawingRoles;
}
