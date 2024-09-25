
package com.github.hiwepy.ton4j.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.hiwepy.ton4j.entity.ConfigChainDailySign;

import java.util.List;

public interface ConfigChainDailySignService extends IService<ConfigChainDailySign> {

    List<ConfigChainDailySign> getChainDailySignInfos();

}
