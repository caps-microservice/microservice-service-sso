package org.caps.microservice.service.sso.service.impl;

import org.apache.commons.lang.StringUtils;
import org.caps.microservice.common.domain.TbUser;
import org.caps.microservice.common.utils.MD5Utils;
import org.caps.microservice.common.utils.MapperUtils;
import org.caps.microservice.service.sso.mapper.TbUserMapper;
import org.caps.microservice.service.sso.service.LoginService;
import org.caps.microservice.service.sso.service.consumer.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    @Transactional(readOnly = false)
    public TbUser login(String phone, String password) {
        TbUser tbUser=null;
        String json = redisCacheService.get(phone);
        //缓存中有数据
        if(StringUtils.isNotBlank(json)){
            try {
                tbUser = MapperUtils.json2pojo(json, TbUser.class);
            } catch (Exception e) {
                logger.warn("触发熔断：{}", e.getMessage());
            }
        }
        //缓存中没有数据
        else {
            //从数据库查询
            Example example=new Example(TbUser.class);
            example.createCriteria().andEqualTo("phone",phone)
                                    .andEqualTo("password",MD5Utils.md5(password));
            tbUser = tbUserMapper.selectOneByExample(example);
            if(tbUser!=null){
                try {
                    redisCacheService.put(phone, MapperUtils.obj2json(tbUser), 60 * 60 * 24);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return tbUser;

    }
    /**
     * 判断手机号是否被注册
     * @param phone
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Integer count(String phone) {
        return null;
    }
}
