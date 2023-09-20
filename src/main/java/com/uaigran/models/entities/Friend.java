package com.uaigran.models.entities;

import com.uaigran.models.entities.User.User;
import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "friends")
public class Friend {

    @EmbeddedId
    private FriendPrimaryKey id;

    @ManyToOne
    @MapsId("owner")
    private User owner;

    @ManyToOne
    @MapsId("friend")
    private User friend;

}

