package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDrawingRole {
    @EmbeddedId
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
