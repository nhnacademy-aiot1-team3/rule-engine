package live.databo3.ruleengine.ai.service;

public interface RedisSaveService {
    void saveRedisWithOrganuzationName(String organzationName,String hashKey, Object value);

}
