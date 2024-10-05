package org.nexus.virtual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2024/9/19 15:32
 * @description
 */
public class RecordDockerRunnable implements Runnable {

    public List<Double> cpuUsages = new ArrayList<Double>();

    public List<Double> memoryUsages = new ArrayList<Double>();

    @Override
    public void run() {
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
//                    String containerId = stats[0];
                    String cpuUsage = stats[2].replace("%", "");  // 移除百分号
                    String memoryUsage = stats[6];  // 当前内存使用
                    double cpuValue = Double.parseDouble(cpuUsage);
                    // 处理内存使用率数据
                    double memoryValue = parseMemoryUsage(memoryUsage);
                    cpuUsages.add(cpuValue);
                    memoryUsages.add(memoryValue);
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
                return Double.parseDouble(usedMemory) * (memoryUsage.contains("GiB") ? 1024 : 1);  // 转换 GiB 为 MB
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

}
