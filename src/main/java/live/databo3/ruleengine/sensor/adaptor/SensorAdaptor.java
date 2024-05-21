package live.databo3.ruleengine.sensor.adaptor;

import live.databo3.ruleengine.event.message.TopicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "sensor-service")
public interface SensorAdaptor {

    @GetMapping("/org/{organizationName}/config")
    ResponseEntity<Void> reloadRedis(@PathVariable String organizationName);

    @PostMapping("/sensor")
    ResponseEntity<Void> saveSensor(@RequestBody TopicDto topicDto);

    @GetMapping("/sensorTypes")
    ResponseEntity<Void> getSensorTypes();
}
