package live.databo3.ruleengine.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import live.databo3.ruleengine.event.message.TopicDto;
import live.databo3.ruleengine.sensor.adaptor.SensorAdaptor;
import live.databo3.ruleengine.service.OrganizationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationInfoServiceImpl implements OrganizationInfoService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SensorAdaptor sensorAdaptor;
    private final ObjectMapper objectMapper;

    public void getSensorListAndAddSensor(TopicDto topic) throws JsonProcessingException {
        synchronized (this) {
            if (Boolean.FALSE.equals(redisTemplate.hasKey("sensorList"))) {
                sensorAdaptor.getSensorTypes();
            }
        }
        List<String> sensorList = redisTemplate.opsForList().range("sensorList", 0, -1).stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        if (Boolean.FALSE.equals(sensorList.contains(topic.getEndpoint()))) {
            return;
        }


        String organizationName = topic.getBranch();
        String deviceName = topic.getDevice();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(organizationName))) {
            sensorAdaptor.reloadRedis(organizationName);
        }

        Map<String, Object> collect = objectMapper.convertValue(redisTemplate.opsForHash().entries(organizationName), new TypeReference<>() {
        });

        Object deviceInfo = collect.get("sensorList:" + deviceName);


        try {
            if (Objects.isNull(deviceInfo)) {
                sensorAdaptor.saveSensor(topic);
                return;
            }
            List<String> sensorTypeList = objectMapper.readValue(deviceInfo.toString(), new TypeReference<>() {
            });
            if (!sensorTypeList.contains(topic.getEndpoint())) {
                sensorAdaptor.saveSensor(topic);
            }

        } catch (FeignException.InternalServerError error) {
            log.error("{} : {} : {}", topic, deviceInfo, error.getMessage());
        } catch (NullPointerException e) {
            log.error("{} : {} : {} : {}","NullPoint", topic, deviceInfo, e.getMessage());
        }
    }
}
