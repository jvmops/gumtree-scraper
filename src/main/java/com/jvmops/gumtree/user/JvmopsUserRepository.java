package com.jvmops.gumtree.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface JvmopsUserRepository extends MongoRepository<JvmopsUser, String> {
    Optional<JvmopsUser> findByEmail(String username);
}
