package live.databo3.ruleengine.node;

import live.databo3.ruleengine.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

@Slf4j
public class DebugNode extends OutputNode {
    public DebugNode() {
        super();
    }

    @Override
    public void init() {

    }

    @Override
    public void process() {
        getInputWires().forEach(wire -> {
            try {
                BlockingQueue<MessageDto> messageQueue = wire.get();
                MessageDto messageDto = messageQueue.take();
//                log.info("message: {}", messageDto);
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
