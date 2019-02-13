package org.caps.microservice.service.sso.mapper;

import org.caps.microservice.common.domain.TbUser;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.MyMapper;

@Repository
public interface TbUserMapper extends MyMapper<TbUser> {
}