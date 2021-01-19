package com.effectivesoft.usermanagement.dao;

import com.effectivesoft.usermanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import javax.persistence.TemporalType;
import java.util.Date;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
    Page<User> findAllByUsername(Pageable pageable, String username);

    @Query("SELECT u FROM User u WHERE u.description LIKE %:descriptionKey%")
    Page<User> findAllByDescription(Pageable pageable, @Param("descriptionKey") String descriptionKey);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:emailKey%")
    Page<User> findAllByEmail(Pageable pageable, @Param("emailKey") String emailKey);
    Page<User> findAllByLastLogonBetween(Pageable pageable, @Temporal(TemporalType.TIMESTAMP) Date lastLogonStart,
                                         @Temporal(TemporalType.TIMESTAMP) Date lastLogonEnd);
    Page<User> findAllByLocked(Pageable pageable, Boolean locked);
    Page<User> findAllByAdmin(Pageable pageable, Boolean admin);

}
