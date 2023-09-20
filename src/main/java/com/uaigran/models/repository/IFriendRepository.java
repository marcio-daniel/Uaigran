package com.uaigran.models.repository;

import com.uaigran.models.entities.Friend;
import com.uaigran.models.entities.FriendPrimaryKey;
import com.uaigran.models.entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IFriendRepository extends JpaRepository<Friend, FriendPrimaryKey> {
    @Override
    Optional<Friend> findById(FriendPrimaryKey friendPrimaryKey);

    List<Friend> findByOwner(User owner);


}
