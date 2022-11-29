package com.app.employee.service;

import com.app.employee.model.common.AuthResponse;
import com.app.employee.model.dto.UserInfoDto;
import com.app.employee.model.request.LoginRequest;

public interface UserInfoService {

    AuthResponse addUserInfoData(LoginRequest loginRequest, String secretKey);

    UserInfoDto getUserInfo(String userId);
}