package com.example.movierating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserEntry, Integer> {
    List<UserEntry> findByName(String name);
    List<UserEntry> findByEmail(String email);

    List<UserEntry> findById(String id);


}
