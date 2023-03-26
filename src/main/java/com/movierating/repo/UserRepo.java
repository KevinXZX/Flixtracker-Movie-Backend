package com.movierating.repo;

import com.movierating.obj.UserEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserEntry, Integer> {
    /**
     * @param name name of the user
     * @return List of the users with the given name
     */
    List<UserEntry> findByName(String name);

    /**
     * @param email email of the user
     * @return List of the users with the given email
     */
    List<UserEntry> findByEmail(String email);

    /**
     * @param id id of the user
     * @return List of the users with the given id
     */
    List<UserEntry> findById(String id);

}
