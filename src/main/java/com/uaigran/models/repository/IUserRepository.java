package com.uaigran.models.repository;

import com.uaigran.models.entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User,UUID> {

    UserDetails findByEmail(String email);

    @Override
    Optional<User> findById(UUID uuid);
}
