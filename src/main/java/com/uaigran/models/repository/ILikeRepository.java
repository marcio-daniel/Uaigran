package com.uaigran.models.repository;

import com.uaigran.models.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ILikeRepository extends JpaRepository<Like, UUID> {
}
