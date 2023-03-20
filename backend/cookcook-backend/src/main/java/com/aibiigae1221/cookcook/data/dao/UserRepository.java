package com.aibiigae1221.cookcook.data.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aibiigae1221.cookcook.data.entity.User;

public interface UserRepository extends JpaRepository<User, UUID>{

	User findByEmail(String email);

}
