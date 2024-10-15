package com.sparta.springtrello.domain.user.repository;

import com.sparta.springtrello.domain.user.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);

//    default User findByEmailOrElseThrow(String email) {
//        return this.findByEmail(email).orElseThrow(()->new UserNotFoundException("유저를 찾을수 없습니다"));
//    }
}
