package shop.rns.receiver.repository.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import shop.rns.receiver.config.exception.BaseException;
import shop.rns.receiver.domain.Broker;
import shop.rns.receiver.domain.KakaoBroker;
import shop.rns.receiver.repository.BrokerRepository;
import shop.rns.receiver.repository.KakaoBrokerRepository;

import static shop.rns.receiver.config.response.BaseResponseStatus.NOT_EXIST_BROKER;


@Slf4j
@Component
@RequiredArgsConstructor
public class BrokerCacheRepository {
    private final KakaoBrokerRepository kakaoBrokerRepository;

    private final BrokerRepository brokerRepository;

    @Cacheable(value = "Broker", key = "#brokerId")
    public Broker findBrokerById(long brokerId){
        return brokerRepository.findById(brokerId).orElseThrow(() -> new BaseException(NOT_EXIST_BROKER));
    }

    @Cacheable(value = "KakaoBroker", key = "#brokerId")
    public KakaoBroker findKakaoBrokerById(long brokerId){
        return kakaoBrokerRepository.findById(brokerId).orElseThrow(() -> new BaseException(NOT_EXIST_BROKER));
    }
}
