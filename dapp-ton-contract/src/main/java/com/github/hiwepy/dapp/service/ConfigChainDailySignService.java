
package com.github.hiwepy.dapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.hiwepy.dapp.entity.ConfigChainDailySign;

import java.util.List;

public interface ConfigChainDailySignService extends IService<ConfigChainDailySign> {

    List<ConfigChainDailySign> getChainDailySignInfos();

}
