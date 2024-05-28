package live.databo3.ruleengine.service;

public interface ControlMessageService {
    void controlMessagePublish(String deviceId, String state);
}
