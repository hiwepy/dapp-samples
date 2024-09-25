package com.github.hiwepy.dapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.hiwepy.dapp.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
