package com.uaigran.models.repository;

import com.uaigran.models.entities.Post;
import com.uaigran.models.entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByUser(User user);

    @Override
    Optional<Post> findById(UUID uuid);
}
