package shop.rns.receiver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import shop.rns.receiver.config.exception.BaseException;
import shop.rns.receiver.config.status.BaseStatus;
import shop.rns.receiver.config.type.MessageType;
import shop.rns.receiver.domain.Member;
import shop.rns.receiver.domain.Point;
import shop.rns.receiver.dto.point.get.GetPointRes;
import shop.rns.receiver.repository.MemberRepository;
import shop.rns.receiver.repository.PointRepository;

import java.util.Optional;

import static shop.rns.receiver.config.response.BaseResponseStatus.INSUFFICIENT_POINT;
import static shop.rns.receiver.config.response.BaseResponseStatus.NOT_EXIST_MEMBER;


@Log4j2
@Service
@RequiredArgsConstructor
public class PointService {
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    // 포인트 조회
    public GetPointRes getPoint(long memberId) {
        Member member = getExistMember(memberId);
        Point point = getExistPointOrMakeDefault(member);
        return GetPointRes.toDto(point);
    }

    // 포인트 충전
    public GetPointRes chargePoint(long memberId, int smsPoint, int kakaoPoint) {
        Member member = getExistMember(memberId);
        Point point = getExistPointOrMakeDefault(member);
        point.addSmsPoint(smsPoint);
        point.addKakaoPoint(kakaoPoint);
        return GetPointRes.toDto(pointRepository.save(point));
    }

    // 메시지 포인트 환불
    public int refundMessagePoint(Member member, int amount, MessageType messageType){
        int weight = messageType == MessageType.SMS? 1 : messageType == MessageType.LMS? 3 : 6;
        Point point = getExistPointOrMakeDefault(member);

        int refundSmsPoint = amount * weight;
        point.addSmsPoint(refundSmsPoint);

        return refundSmsPoint;
    }

    // 포인트 검증
    public GetPointRes validPoint(long memberId, int smsPoint, int kakaoPoint) {
        Member member = getExistMember(memberId);
        Point point = getExistPointOrMakeDefault(member);
        if (point.getSmsPoint() < smsPoint || point.getKakaoPoint() < kakaoPoint)
            throw new BaseException(INSUFFICIENT_POINT);
        return GetPointRes.toDto(point);
    }

    // 포인트 결제 (SMS)
    public GetPointRes paySmsPoint(long memberId, int smsPoint) {
        Member member = getExistMember(memberId);
        Point point = getExistPointOrMakeDefault(member);
        if (point.getSmsPoint() < smsPoint)
            throw new BaseException(INSUFFICIENT_POINT);
        else
            point.subSmsPoint(smsPoint);
        return GetPointRes.toDto(pointRepository.save(point));
    }

    // 포인트 결제 (KAKAO)
    public GetPointRes payKakaoPoint(long memberId, int kakaoPoint) {
        Member member = getExistMember(memberId);
        Point point = getExistPointOrMakeDefault(member);
        if (point.getKakaoPoint() < kakaoPoint)
            throw new BaseException(INSUFFICIENT_POINT);
        else
            point.subKakaoPoint(kakaoPoint);
        return GetPointRes.toDto(pointRepository.save(point));
    }

    private Member getExistMember(long memberId) {
        return memberRepository.findByIdAndStatus(memberId, BaseStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_EXIST_MEMBER));
    }

    private Point getExistPointOrMakeDefault(Member member) {
        Optional<Point> byMemberId = pointRepository.findByMemberId(member.getId());
        if (byMemberId.isEmpty()) {
            return pointRepository.save(Point.builder()
                            .member(member)
                            .smsPoint(0)
                            .kakaoPoint(0)
                            .build());
        } else {
            return byMemberId.get();
        }
    }
}
