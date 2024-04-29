package live.databo3.ruleengine.ex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class QueueReader {
    private BlockingQueue<Integer> queue;

    public QueueReader(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    public void startReading() {
        Thread thread = new Thread(() -> {

            try {
                Integer take = queue.take();
                System.out.println("received " + take);
            } catch (InterruptedException e) {


            }
//            while (true) {
//                try {
//                    Integer message = queue.take();
//                    System.out.println("received " + message);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
        });
        thread.start();
    }

    private void processMessage(final Integer message) {
        // 메시지 처리 로직 구현
        System.out.println("Received message: " + message);
    }

    public void send(Integer message) throws InterruptedException {
        queue.put(message);
        System.out.println("sending message: " + message);
        Thread thread = new Thread(() -> {
            try {
                System.out.println("Sent message: " + message);
                queue.put(message);

            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted");
            }
        });
        thread.start();
    }

    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(100);
        QueueReader reader = new QueueReader(queue);
        System.out.println("Sending message...");
        for (int i = 1; i < 30; i++) {
            reader.send(i);
        }
        reader.startReading();



//        ObjectMapper objectMapper = new ObjectMapper();
//        String s = "{\"name\":\"yang\",\"id\":\"h\"}";
////        TestDto testDto = new TestDto("1", "2");
////        System.out.println(testDto);
//        TestDto testDto = objectMapper.readValue(s, TestDto.class);
//
//        System.out.println(testDto);
    }

}
