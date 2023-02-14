package shop.rns.receiver.dto.dlx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.rns.receiver.dto.KakaoMessageDto;
import shop.rns.receiver.dto.message_result.KakaoMessageResultDto;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReceiveKakaoMessageDto {
    private KakaoMessageDto kakaoMessageDto;
    private KakaoMessageResultDto kakaoMessageResultDto;
}
