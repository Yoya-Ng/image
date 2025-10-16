package com.example.demo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 這裡可以擴展更多自定義方法，例如根據姓名查詢
    User findByName(String name);
}
