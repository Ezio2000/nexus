package org.nexus;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Xieningjun
 * @date 2024/9/14 17:35
 * @description
 */
public class DockerStatsChart {

    private static final Map<String, XYSeries> cpuSeriesMap = new HashMap<>();
    private static final Map<String, XYSeries> memorySeriesMap = new HashMap<>();
    private static long startTime = System.currentTimeMillis();
    private static double maxCpu = 0d;
    private static double maxMemory = 0d;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Docker CPU and Memory Usage Chart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());

            // 创建数据集
            XYSeriesCollection cpuDataset = new XYSeriesCollection();
            XYSeriesCollection memoryDataset = new XYSeriesCollection();

            // 创建图表
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Docker CPU / Memory Usage",
                    "Time (s)",
                    "CPU Usage (%)",
                    cpuDataset,
                    PlotOrientation.VERTICAL,
                    true, true, false);

            // 获取图表的绘图区域
            XYPlot plot = chart.getXYPlot();

            // 设置背景颜色为白色
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainGridlinePaint(Color.BLACK);
            plot.setRangeGridlinePaint(Color.BLACK);

            // 创建 CPU 使用率的渲染器并设置颜色
            XYLineAndShapeRenderer cpuRenderer = new XYLineAndShapeRenderer();
            cpuRenderer.setSeriesPaint(0, Color.BLACK); // CPU 使用率线的颜色
//            cpuRenderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
            plot.setRenderer(0, cpuRenderer);

            // 创建内存使用率的 Y 轴
            NumberAxis memoryAxis = new NumberAxis("Memory Usage (MB)");
            plot.setRangeAxis(1, memoryAxis);
            plot.setDataset(1, memoryDataset);
            plot.mapDatasetToRangeAxis(1, 1);  // 将 memoryDataset 映射到第二个 Y 轴

            // 创建内存使用率的渲染器并设置颜色
            XYLineAndShapeRenderer memoryRenderer = new XYLineAndShapeRenderer();
            memoryRenderer.setSeriesPaint(0, Color.RED); // 内存使用率线的颜色
//            memoryRenderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
            plot.setRenderer(1, memoryRenderer);

            // 创建图表面板并设置显示
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 500));
            frame.add(chartPanel, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

            // 添加鼠标监听器
            chartPanel.addChartMouseListener(new ChartMouseListener() {
                @Override
                public void chartMouseClicked(ChartMouseEvent event) {
                    // 判断是否点击到一个数据点上
                    if (event.getEntity() instanceof XYItemEntity) {
                        XYItemEntity entity = (XYItemEntity) event.getEntity();
                        XYDataset dataset = entity.getDataset();
                        int seriesIndex = entity.getSeriesIndex();
                        int itemIndex = entity.getItem();

                        // 获取点击点的时间和值
                        double xValue = dataset.getXValue(seriesIndex, itemIndex);
                        double yValue = dataset.getYValue(seriesIndex, itemIndex);

                        String seriesName = dataset.getSeriesKey(seriesIndex).toString();
                        JOptionPane.showMessageDialog(frame,
                                "Series: " + seriesName + "\nTime: " + xValue + " s\nValue: " + yValue,
                                "Data Point Information",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                @Override
                public void chartMouseMoved(ChartMouseEvent event) {
                    // 鼠标移动时不做处理
                }
            });

            // 创建定时任务来更新图表
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

            Runnable task = () -> {
                try {
                    Process process = Runtime.getRuntime().exec("docker stats --no-stream");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    boolean firstLine = true;  // 跳过第一行表头
                    while ((line = reader.readLine()) != null) {
                        if (firstLine) {
                            firstLine = false;
                            continue;
                        }
                        // 解析并打印 CPU 和内存使用率
                        String[] stats = line.trim().split("\\s+");
                        if (stats.length >= 7) {
                            String containerId = stats[0];
                            String cpuUsage = stats[2].replace("%", "");  // 移除百分号
                            String memoryUsage = stats[6];  // 当前内存使用
                            double cpuValue;
                            try {
                                cpuValue = Double.parseDouble(cpuUsage);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                continue;
                            }

                            // 处理内存使用率数据
                            double memoryValue = parseMemoryUsage(memoryUsage);

                            // 更新数据集中的对应折线
                            XYSeries cpuSeries = cpuSeriesMap.get(containerId);
                            if (cpuSeries == null) {
                                cpuSeries = new XYSeries(containerId + " CPU");
                                cpuSeriesMap.put(containerId, cpuSeries);
                                cpuDataset.addSeries(cpuSeries);
                            }
                            cpuSeries.add((System.currentTimeMillis() - startTime) / 1000.0, cpuValue);

                            XYSeries memorySeries = memorySeriesMap.get(containerId);
                            if (memorySeries == null) {
                                memorySeries = new XYSeries(containerId + " Memory");
                                memorySeriesMap.put(containerId, memorySeries);
                                memoryDataset.addSeries(memorySeries);
                            }
                            memorySeries.add((System.currentTimeMillis() - startTime) / 1000.0, memoryValue);

                            if (cpuValue > maxCpu) {
                                maxCpu = cpuValue;
                            }
                            if (memoryValue > maxMemory) {
                                maxMemory = memoryValue;
                            }
                            System.out.println(
                                    "最大CPU: " + maxCpu + "\n" + "最大MEMORY: " + maxMemory
                            );
                        }
                    }

                    process.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            };

            // 每 100 毫秒更新一次图表
            executorService.scheduleAtFixedRate(task, 0, 100, TimeUnit.MILLISECONDS);
        });
    }

    private static double parseMemoryUsage(String memoryUsage) {
        // 解析内存使用量
        try {
            // 假设内存使用格式是 "1.5GiB / 4GiB" 或类似格式
            String[] parts = memoryUsage.split(" / ");
            if (parts.length > 0) {
                String usedMemory = parts[0].replace("GiB", "")
                        .replace("MiB", "")
                        .replace("%", "")
                        .trim();
                return Double.parseDouble(usedMemory) * (parts[0].contains("GiB") ? 1024 : 1);  // 转换 GiB 为 MB
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
