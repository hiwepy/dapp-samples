package com.github.hiwepy.dapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.hiwepy.dapp.entity.User;
import com.github.hiwepy.dapp.mapper.UserMapper;
import com.github.hiwepy.dapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
