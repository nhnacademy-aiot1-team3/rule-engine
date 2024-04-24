package live.databo3.ruleengine.node;

import lombok.extern.slf4j.Slf4j;

public interface Node extends Runnable {

    public void init() ;
    public void process() ;


    public void start();
}
