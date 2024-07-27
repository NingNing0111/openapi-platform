package me.pgthinker.service.inner;

import com.baomidou.mybatisplus.extension.service.IService;
import me.pgthinker.model.entity.InterfaceInfo;

/**
 * @Project: me.pgthinker.service.inner
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/28 00:22
 * @Description:
 */
public interface ApiInfoService extends IService<InterfaceInfo> {
    /**
     * 地址+方法 匹配接口信息并返回
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo matchInterfaceInfo(String url, String method);
}
