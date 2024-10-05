package org.nexus.metrics;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.nexus.virtual.IOTest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author Xieningjun
 */
@RestController
@RequestMapping("/io")
public class MetricsController {

    public PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    @PostConstruct
    public void init() {
        Metrics.addRegistry(prometheusRegistry);
    }

    public void metrics(String metricsKey, Runnable runnable) {
        Metrics.addRegistry(prometheusRegistry);
        Timer timer = Timer.builder(metricsKey)
                .publishPercentileHistogram(false)
                .publishPercentiles()
//                .maximumExpectedValue(Duration.ofSeconds(2))
//                .distributionStatisticBufferLength(1)
//                .sla(Duration.ofMillis(50), Duration.ofMillis(100), Duration.ofMillis(200), Duration.ofMillis(500), Duration.ofSeconds(1))
                .register(Metrics.globalRegistry);
        // 示例计时
        timer.record(runnable);
    }

    @RequestMapping("/virtual")
    public String ioVirtual(@RequestParam(value = "c") int concurrent, @RequestParam(value = "m") int mockTime) {
        metrics("io.virtual." + concurrent + "." + mockTime, () -> {
            IOTest.virtual(concurrent, mockTime);
        });
        return prometheusRegistry.scrape();
    }

    @RequestMapping("/plat")
    public String ioPlat(@RequestParam(value = "c") int concurrent, @RequestParam(value = "m") int mockTime) {
        metrics("io.plat." + concurrent + "." + mockTime, () -> {
            IOTest.plat(Runtime.getRuntime().availableProcessors() * 2, concurrent, mockTime);
        });
        return prometheusRegistry.scrape();
    }

    @RequestMapping("/scrape")
    public String ioScrape() {
        return prometheusRegistry.scrape();
    }

    @RequestMapping("/gc")
    public String ioGc() {
        System.gc();
        return prometheusRegistry.scrape();
    }

//    public static void main(String[] args) {
//        MetricsController metricsController = new MetricsController();
//        metricsController.ioVirtual();
//        metricsController.ioPlat();
//        // 输出到标准输出以供 Prometheus 抓取
//        System.out.println(metricsController.prometheusRegistry.scrape());
//    }

}
