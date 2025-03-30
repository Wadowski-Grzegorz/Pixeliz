package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy="role")
    private Set<UserDrawingRole> userDrawingRoles;
}
