package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 取得所有使用者
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 根據 ID 取得使用者
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // 根據名稱取得使用者
    @GetMapping("/name/{name}")
    public User getUserByName(@PathVariable String name) {
        return userRepository.findByName(name);
    }

    // 建立新使用者
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // 更新使用者
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setName(userDetails.getName());
        user.setAge(userDetails.getAge());
        
        return userRepository.save(user);
    }

    // 刪除使用者
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully!";
    }
}
