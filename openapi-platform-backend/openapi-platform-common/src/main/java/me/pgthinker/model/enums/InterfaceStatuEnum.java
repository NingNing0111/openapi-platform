package me.pgthinker.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Pg Thinker
 * @Description: 描述接口状态的枚举类
 */
@RequiredArgsConstructor
@Getter
public enum InterfaceStatuEnum {

    /**
     * 未开启 接口创建时默认值
     */
    NOT_STARTED("未开启",0),
    /**
     * 开启，即上线
     */
    RUNNING("开启",1),
    /**
     * 维护中，后期拓展
     */
    UNDER_MAINTENANCE("维护中",2),
    /**
     * 过时，后期拓展
     */
    OUTDATED("过时",3);

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
    public static InterfaceStatuEnum getEnumByValue(int value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (InterfaceStatuEnum anEnum : InterfaceStatuEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }

}
