package com.sorinbratosin.licenta.Database;


import com.sorinbratosin.licenta.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
