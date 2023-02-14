package shop.rns.receiver.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import shop.rns.receiver.config.status.MessageStatus;
import shop.rns.receiver.config.type.ButtonType;
import shop.rns.receiver.domain.KakaoMessage;
import shop.rns.receiver.domain.Member;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KakaoMessageDto {
    private String from;

    @JsonIgnore
    private String to;

    private String title;

    private String subtitle;

    private String content;

    private String description;

    private String image;

    private MessageStatus messageStatus;

    private String buttonTitle;

    private String buttonUrl;

    private ButtonType buttonType;

    private String cronExpression;

    private String cronText;

    public static KakaoMessage toEntity(KakaoMessageDto dto, Member member) {
        return KakaoMessage.builder()
                .member(member)
                .sender(dto.getFrom())
                .title(dto.getTitle())
                .subTitle(dto.getSubtitle())
                .content(dto.getContent())
                .image(dto.getImage())
                .description(dto.getDescription())
                .buttonTitle(dto.getButtonTitle())
                .buttonUrl(dto.getButtonUrl())
                .buttonType(dto.getButtonType())
                .build();
    }

    public static KakaoMessageDto toDto(KakaoMessage kakaoMessage) {
        return KakaoMessageDto.builder()
                .title(kakaoMessage.getTitle())
                .subtitle(kakaoMessage.getSubTitle())
                .content(kakaoMessage.getContent())
                .image(kakaoMessage.getImage())
                .description(kakaoMessage.getDescription())
                .buttonTitle(kakaoMessage.getButtonTitle())
                .buttonUrl(kakaoMessage.getButtonUrl())
                .buttonType(kakaoMessage.getButtonType())
                .build();
    }
}
