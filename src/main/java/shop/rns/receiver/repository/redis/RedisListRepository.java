package shop.rns.receiver.repository.redis;

import java.util.Collection;

public interface RedisListRepository {
    public void rightPush(String key, Object value);

    public void rightPushAll(String key, Collection values, int duration);

    public String leftPop(String key);

    public void remove(String key);
}
