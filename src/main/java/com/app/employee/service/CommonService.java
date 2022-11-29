package com.app.employee.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface CommonService {
    Optional<String> getCurrentUser(HttpServletRequest httpServletRequest);
}
