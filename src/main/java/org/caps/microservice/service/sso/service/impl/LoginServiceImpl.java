package org.caps.microservice.service.sso.service.impl;

import org.caps.microservice.common.domain.TbUser;
import org.caps.microservice.service.sso.service.LoginService;

import java.util.Map;

public class LoginServiceImpl implements LoginService {
    @Override
    public int userRegister(TbUser user) {
        return 0;
    }

    @Override
    public Map<String, Object> login(String phone, String password) {
        return null;
    }

    @Override
    public Integer count(String phone) {
        return null;
    }
}
