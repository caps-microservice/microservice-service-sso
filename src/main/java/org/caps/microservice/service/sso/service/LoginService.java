package org.caps.microservice.service.sso.service;

import org.caps.microservice.common.domain.TbUser;

import java.util.Map;

public interface LoginService {
    /**
     * 注册
     * @param user
     * @return
     */
    int userRegister(TbUser user);

    /**
     * 登陆
     * @param phone
     * @param password
     * @return
     */
    Map<String,Object> login(String phone, String password);
    /**
     * 用户数量
     * @param phone
     * @return
     */
    Integer count(String phone);
}
