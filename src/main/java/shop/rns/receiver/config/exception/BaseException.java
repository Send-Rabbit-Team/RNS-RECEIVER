package shop.rns.receiver.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.rns.receiver.config.response.BaseResponseStatus;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends RuntimeException{
    private BaseResponseStatus status;
}
