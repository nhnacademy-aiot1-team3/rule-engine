package live.databo3.ruleengine.node;

import live.databo3.ruleengine.dto.DataPayloadDto;
import live.databo3.ruleengine.dto.MessageDto;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;

public class NullCheckNode extends InputOutputNode {

    @Override
    public void init() {

    }

    @Override
    public void process() {
        getInputWires().forEach(wire -> {
            try {
                BlockingQueue<MessageDto> messageQueue = wire.get();
                MessageDto<String, DataPayloadDto> messageDto = messageQueue.take();

                if (Objects.nonNull(messageDto.getPayload().getValue())) {
                    output(messageDto);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            process();
        }
    }
}
