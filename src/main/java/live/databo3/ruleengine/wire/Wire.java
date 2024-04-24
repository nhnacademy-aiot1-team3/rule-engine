package live.databo3.ruleengine.wire;

import live.databo3.ruleengine.dto.MessageDto;

import java.util.concurrent.BlockingQueue;

public interface Wire {
    public void put(MessageDto messageDto) throws InterruptedException;

    public boolean hasMessage();

    public BlockingQueue<MessageDto> get() throws InterruptedException;
}
