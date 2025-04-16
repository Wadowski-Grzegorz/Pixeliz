package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Drawing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String grid;
    private String name;
    private int size_x;
    private int size_y;

    @JsonIgnore
    @OneToMany(mappedBy = "drawing")
    Set<UserDrawingRole> userDrawingRoles;
}
