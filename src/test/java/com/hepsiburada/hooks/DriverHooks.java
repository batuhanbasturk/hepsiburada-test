package com.hepsiburada.hooks;

import com.hepsiburada.driver.DriverFactory;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.ExecutionContext;
import com.thoughtworks.gauge.Gauge;

public class DriverHooks {

    @BeforeScenario
    public void startBrowser() {
        DriverFactory.createDriver();
    }

    @AfterScenario
    public void stopBrowser(ExecutionContext context) {
        if (Boolean.TRUE.equals(context.getCurrentScenario().getIsFailing())) {
            Gauge.captureScreenshot();
        }
        DriverFactory.quitDriver();
    }
}
