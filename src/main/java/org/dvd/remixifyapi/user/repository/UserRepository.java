package org.dvd.remixifyapi.user.repository;

import org.dvd.remixifyapi.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.recipes WHERE u.username = :username")
    Optional<User> findByUsernameWithRecipes(@Param("username") String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.likedRecipes WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.recipes")
    List<User> findAllWithRecipes();
}

