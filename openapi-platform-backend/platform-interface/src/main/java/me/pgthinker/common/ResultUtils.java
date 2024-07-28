package me.pgthinker.common;

/**
 * @author bodyzxy
 * @github https://github.com/bodyzxy
 * @date 2024/7/27 16:07
 */
public class ResultUtils {

    public static <T> BaseResponse<T> success(T data) {return new BaseResponse<>(0,data,"ok");}

    public static BaseResponse error(ErrorCode errorCode) {return new BaseResponse<>(errorCode);}

    public static BaseResponse error(int code, String message) {
        return new BaseResponse(code, null, message);
    }

    public static BaseResponse error(ErrorCode errorCode, String message) {
        return new BaseResponse(errorCode.getCode(), null, message);
    }
}
