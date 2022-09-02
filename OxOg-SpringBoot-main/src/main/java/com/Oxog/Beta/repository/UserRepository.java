package com.Oxog.Beta.repository;


import com.Oxog.Beta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User,Long> {


}
