package org.nexus.metrics;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Random;

/**
 * @author Xieningjun
 */
@RestController
public class MetricsController {

    private Random rand = new Random();

    PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    @RequestMapping("/metrics")
    public void metrics() {
        Metrics.addRegistry(prometheusRegistry);

        Timer timer = Timer.builder("nexus.timer")
                .publishPercentileHistogram(false)
                .publishPercentiles(0.9, 0.99)
                .maximumExpectedValue(Duration.ofSeconds(2))
                .distributionStatisticBufferLength(1)
//                .sla(Duration.ofMillis(50), Duration.ofMillis(100), Duration.ofMillis(200), Duration.ofMillis(500), Duration.ofSeconds(1))
                .register(Metrics.globalRegistry);

        // 示例计时
        timer.record(() -> {
            try {
                Thread.sleep(150); // 模拟任务
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 输出到标准输出以供 Prometheus 抓取
        System.out.println(prometheusRegistry.scrape());
    }

}
