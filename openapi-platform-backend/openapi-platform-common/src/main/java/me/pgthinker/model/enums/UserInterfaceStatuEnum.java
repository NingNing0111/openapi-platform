package me.pgthinker.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project: com.pgthinker.backend.model.enums
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/27 14:18
 * @Description:
 */
@Getter
@RequiredArgsConstructor
public enum UserInterfaceStatuEnum {
    /**
     * 禁用，即不允许用户调用
     */
    Disabled("禁用",1),
    /**
     * 开启，即允许用户调用
     */
    Enabled("开启",0);


    // 描述信息
    private final String name;
    // 值
    private final int value;

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static UserInterfaceStatuEnum getEnumByValue(int value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserInterfaceStatuEnum anEnum : UserInterfaceStatuEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }
}
