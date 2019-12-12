package io.yzecho.yimcommon.exception;

import io.yzecho.yimcommon.enums.StatusEnum;

/**
 * @author yzecho
 * @desc
 * @date 10/12/2019 08:12
 */
public class YimException extends GenericException {
    public YimException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public YimException(Exception e, String errorCode, String errorMessage) {
        super(e, errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public YimException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public YimException(StatusEnum statusEnum) {
        super(statusEnum.getMessage());
        this.errorMessage = statusEnum.message();
        this.errorCode = statusEnum.getCode();
    }

    public YimException(StatusEnum statusEnum, String message) {
        super(message);
        this.errorMessage = message;
        this.errorCode = statusEnum.getCode();
    }

    public YimException(Exception oriEx) {
        super(oriEx);
    }

    public YimException(Throwable oriEx) {
        super(oriEx);
    }

    public YimException(String message, Exception oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }

    public YimException(String message, Throwable oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }


    public static boolean isResetByPeer(String msg) {
        return "Connection reset by peer".equals(msg);
    }

}
