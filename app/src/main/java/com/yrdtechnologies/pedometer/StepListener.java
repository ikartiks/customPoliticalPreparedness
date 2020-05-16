package com.yrdtechnologies.pedometer;

/**
 * Created by kartikshah on 07/03/18.
 */

// Will listen to step alerts
public interface StepListener {

    void step(long timeNs);
}
