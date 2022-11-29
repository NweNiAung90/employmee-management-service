package com.app.employee.service.impl;

import com.app.employee.constant.Enum;
import com.app.employee.model.common.AuthResponse;
import com.app.employee.model.dto.UserInfoDto;
import com.app.employee.model.entity.UserInfo;
import com.app.employee.model.request.LoginRequest;
import com.app.employee.repository.UserInfoRepository;
import com.app.employee.service.UserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private static final Logger logger = LogManager.getLogger(UserInfoServiceImpl.class);
    @Autowired
    UserInfoRepository userInfoRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public AuthResponse addUserInfoData(LoginRequest loginRequest, String secretKey) {
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(loginRequest.getUserId());
            userInfo.setSecretKey(secretKey);
            userInfoRepository.save(userInfo);
            AuthResponse authResponse = new AuthResponse(Enum.AUTHORIZED, secretKey);
            return authResponse;
        } catch (Exception e) {
            logger.error("DB Exception :" + e.getMessage());
            throw e;
        }
    }

    @Override
    public UserInfoDto getUserInfo(String userId) {
        logger.info("Getting User Information for selected User Id : " + userId);
        UserInfo userInfo = userInfoRepository.findByUserId(userId);
        if (userInfo != null) {
            return modelMapper.map(userInfo, UserInfoDto.class);
        }
        return null;
    }
}
