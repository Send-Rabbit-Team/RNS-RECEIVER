package shop.rns.receiver.dto.dlx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.rns.receiver.dto.message_result.MessageResultDto;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReceiveMessageDto {
    private SmsMessageDto smsMessageDto;

    private MessageResultDto messageResultDto;
}