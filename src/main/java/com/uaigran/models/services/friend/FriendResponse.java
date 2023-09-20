package com.uaigran.models.services.friend;

import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FriendResponse {
    public UUID friend_id;
    public String name;
    public String photoUri;

}
