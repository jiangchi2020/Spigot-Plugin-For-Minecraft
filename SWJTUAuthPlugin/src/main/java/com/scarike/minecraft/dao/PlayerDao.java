package com.scarike.minecraft.dao;

import com.scarike.minecraft.util.LoginStatus;

public interface PlayerDao {
    LoginStatus check(String username);
}
