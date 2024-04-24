package live.databo3.ruleengine.wire;

import live.databo3.ruleengine.dto.MessageDto;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BufferedWire implements Wire{

    private BlockingQueue<MessageDto> messageQueue;

    public BufferedWire() {
//        this.messageQueue = new LinkedBlockingQueue<>(100);
        this.messageQueue = new LinkedBlockingQueue<>(1);
//        this.messageQueue = new ArrayBlockingQueue<>(1);
    }

    @Override
    public BlockingQueue<MessageDto> get() throws InterruptedException {
        return messageQueue;
    }

    @Override
    public boolean hasMessage() {
        return !messageQueue.isEmpty();
    }

    @Override
    public void put(MessageDto messageDto) throws InterruptedException {
        messageQueue.put(messageDto);
    }
}
