package live.databo3.ruleengine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import live.databo3.ruleengine.event.message.TopicDto;

import java.util.List;
import java.util.Map;

public interface OrganizationInfoService {
    void getSensorListAndAddSensor(TopicDto topic ) throws JsonProcessingException;
}
