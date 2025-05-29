package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "user_drawing_role",
        indexes = {
                @Index(name = "idx_user_drawing", columnList = "user_id, drawing_id"),
                @Index(name = "idx_user", columnList = "user_id"),
                @Index(name = "idx_drawing", columnList = "drawing_id")
        }
)
public class UserDrawingRole {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private UserDrawingKey id;

    @ManyToOne
    @MapsId("drawingId")
    @JoinColumn(name="drawing_id", nullable=false)
    @NotNull
    Drawing drawing;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id", nullable=false)
    @NotNull
    User user;

    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    @NotNull
    Role role;

    public UserDrawingRole(User user, Drawing drawing, Role role){
        this.user = user;
        this.drawing = drawing;
        this.role = role;
        this.id = new UserDrawingKey(drawing.getId(), user.getId());
    }
}
