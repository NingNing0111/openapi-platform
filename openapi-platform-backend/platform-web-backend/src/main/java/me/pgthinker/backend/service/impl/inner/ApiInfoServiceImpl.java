package me.pgthinker.backend.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.pgthinker.backend.mapper.InterfaceInfoMapper;
import me.pgthinker.model.entity.InterfaceInfo;
import me.pgthinker.service.inner.ApiInfoService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Project: me.pgthinker.backend.service.impl.inner
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/28 00:47
 * @Description:
 */
@DubboService
public class ApiInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements ApiInfoService {
    @Override
    public InterfaceInfo matchInterfaceInfo(String url, String method) {
        QueryWrapper<InterfaceInfo> qw = new QueryWrapper<>();
        qw.eq("url",url);
        qw.eq("method",method.toUpperCase());
        return this.getOne(qw);
    }
}
