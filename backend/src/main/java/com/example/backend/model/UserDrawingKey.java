package com.example.backend.model;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class UserDrawingKey implements Serializable {
    @Column(name="drawing_id")
    private Long drawingId;

    @Column(name="user_id")
    private Long userId;

}
