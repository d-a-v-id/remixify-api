package org.dvd.remixifyapi.auth.repository;

import java.util.List;
import java.util.Optional;

import org.dvd.remixifyapi.auth.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("""
        select t from Token t 
        where t.user.id = :userId 
        and (t.expired = false or t.revoked = false)
        """)
    List<Token> findAllValidTokenByUser(@Param("userId") Long userId);

    Optional<Token> findByToken(String jwtToken);
}
