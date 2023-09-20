package com.uaigran.models.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FriendPrimaryKey implements Serializable {

    private UUID owner;

    private UUID friend;

}
