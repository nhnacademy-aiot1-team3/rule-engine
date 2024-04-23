package live.databo3.ruleengine.ex;

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
//            Flux.fromIterable(queue)
//                    .subscribeOn(Schedulers.parallel()) // 병렬 스케줄러를 사용하여 비동기적으로 처리
//                    .subscribe(this::processMessage);
//            Flux<Integer> messageFlux = Flux.generate(synchronousSink -> {
//                try {
//                    Integer message = queue.take(); // 블로킹 큐에서 메시지를 가져옴
//                    synchronousSink.next(message); // Flux에 메시지를 emit
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    synchronousSink.error(e); // 에러 처리
//                }
//            });
//
//            messageFlux.subscribe(this::processMessage);
            while (true) {
                try {
                    Integer message = queue.take();
                    System.out.println("received " + message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        // 메시지 Flux를 구독하여 처리
    }

    private void processMessage(final Integer message) {
        // 메시지 처리 로직 구현
        System.out.println("Received message: " + message);
    }

    public void send(Integer message) throws InterruptedException {
        queue.put(message);
        System.out.println("sending message: " + message);
//        Thread thread = new Thread(() -> {
//            try {
//                System.out.println("Sent message: " + message);
//                queue.put(message);
//
//            }catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                System.out.println("Thread interrupted");
//            }
//        });
//        thread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        QueueReader reader = new QueueReader(queue);
        reader.startReading();
        System.out.println("Sending message...");
        for (int i = 0; i < 30; i++) {
            reader.send(i);
        }
    }

}
