package com.aibiigae1221.cookcook.service;

import com.aibiigae1221.cookcook.data.entity.User;

public interface UserService {

	User addUser(User user);

	long countAllUsers();

}
