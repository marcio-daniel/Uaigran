package com.uaigran.models.services.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {

    public UUID owner_id;

    public UUID friend_id;

}
