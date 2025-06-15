package com.praktica.HelpDesk.secutiry;

import com.praktica.HelpDesk.dto.auth.AuthRequestDto;
import com.praktica.HelpDesk.dto.auth.AuthResponseDto;
import com.praktica.HelpDesk.dto.auth.RegisterRequestDto;
import com.praktica.HelpDesk.dto.user.UserResponseDto;
import com.praktica.HelpDesk.entity.UserEntity;

public interface AuthService {
    UserEntity registerUser(RegisterRequestDto registerRequestDto);
    AuthResponseDto authenticateUser(AuthRequestDto authRequestDto);
}

