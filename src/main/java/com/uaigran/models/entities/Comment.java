package com.uaigran.models.entities;

import com.uaigran.models.entities.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "comment_text")
    private String comment_text;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
