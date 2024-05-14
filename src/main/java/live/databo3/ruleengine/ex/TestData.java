//package live.databo3.ruleengine.ex;
//
//import lombok.Getter;
//import org.hibernate.annotations.GenericGenerator;
//import org.hibernate.annotations.Parameter;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Getter
//@Entity
//public class TestData {
//    //    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    @GenericGenerator(
////            name = "test-sequence-generator",
////            strategy = "sequence",
////            parameters = {
////                    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = SequenceStyleGenerator.DEF_SEQUENCE_NAME),
////                    @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM, value = "1"),
////                    @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1000"),
////                    @Parameter(name = AvailableSettings.PREFERRED_POOLED_OPTIMIZER, value = "pooled-lo")
////            }
////    )
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Column(name = "uuid")
//    private String uuid;
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    protected TestData() {
//        this.id = 0L;
//        this.uuid = UUID.randomUUID().toString();
//        this.createdAt = LocalDateTime.now();
//    }
//
//    public static TestData create() {
//        return new TestData();
//    }
//}
