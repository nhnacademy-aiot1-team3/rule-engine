package live.databo3.ruleengine.ai.adaptor;

/**
 * 예측한 값을 저장하는 Adaptor
 *
 * @author 박상진
 * @version 1.0.0
 */
public interface PredictSaveAdaptor {
    /**
     * 온도 예측값을 저장하는 메서드
     *
     * @since 1.0.0
     */
    void predictSaveTemp();

    /**
     * 전기요금 예측값을 저장하는 메서드
     *
     * @since 1.0.0
     */
    void predictSaveElect();
}
