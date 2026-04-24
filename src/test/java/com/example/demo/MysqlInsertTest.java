package com.example.demo;

import com.example.demo.modules.user.entity.QualityDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MysqlInsertTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testInsert10000() {
//        // 1. 建表（如果不存在）
//        String createTable = "CREATE TABLE IF NOT EXISTS quality_detail (" +
//                "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
//                "report_id VARCHAR(32)," +
//                "batch_no VARCHAR(32)," +
//                "process_stage VARCHAR(64)," +
//                "item_name VARCHAR(128)," +
//                "item_category VARCHAR(64)," +
//                "standard_value VARCHAR(128)," +
//                "test_value VARCHAR(128)," +
//                "unit VARCHAR(32)," +
//                "result VARCHAR(16)," +
//                "test_method VARCHAR(128)," +
//                "method_version VARCHAR(64)," +
//                "test_date DATETIME," +
//                "operator VARCHAR(64)," +
//                "reviewer VARCHAR(64)," +
//                "approver VARCHAR(64)," +
//                "equipment_id VARCHAR(64)," +
//                "reagent_lot VARCHAR(64)," +
//                "create_time DATETIME," +
//                "update_time DATETIME," +
//                "remark VARCHAR(256)" +
//                ")";
//        jdbcTemplate.execute(createTable);
//        System.out.println("表创建成功");

        // 2. 批量插入 1 万条数据
        String sql = "INSERT INTO quality_detail (" +
                "report_id, batch_no, process_stage, item_name, item_category, " +
                "standard_value, test_value, unit, result, test_method, method_version, " +
                "test_date, operator, reviewer, approver, equipment_id, reagent_lot, " +
                "create_time, update_time, remark) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 1; i <= 20000; i++) {
            Object[] args = new Object[]{
                    "RPT" + String.format("%08d", i),
                    "B" + System.currentTimeMillis() + i,
                    getProcessStage(i),
                    getItemName(i),
                    getItemCategory(i),
                    getStandardValue(i),
                    getTestValue(i),
                    getUnit(i),
                    i % 5 == 0 ? "不合格" : "合格",
                    "流式细胞术",
                    "中国药典2025版",
                    Timestamp.valueOf(LocalDateTime.now()),
                    "质检员_" + (i % 10),
                    "复核员_" + (i % 5),
                    "批准员",
                    "设备_" + (i % 20),
                    "试剂批号_" + i,
                    Timestamp.valueOf(LocalDateTime.now()),
                    Timestamp.valueOf(LocalDateTime.now()),
                    "测试数据_" + i
            };
            batchArgs.add(args);
        }

        long start = System.currentTimeMillis();
        jdbcTemplate.batchUpdate(sql, batchArgs);
        long end = System.currentTimeMillis();

        System.out.println("MySQL 插入 1 万条数据（23字段）耗时：" + (end - start) + " ms");
    }
    @Test
    public void testSelectAndMap() {
        // 游标分页取 1 万条
        Long lastId = 0L;
        int limit = 40000;
        String sql = "SELECT id, report_id, batch_no, process_stage, item_name, item_category, " +
                "standard_value, test_value, unit, result, test_method, method_version, " +
                "test_date, operator, reviewer, approver, equipment_id, reagent_lot, " +
                "create_time, update_time, remark " +
                "FROM quality_detail WHERE id > ? ORDER BY id LIMIT ?";

        long start = System.currentTimeMillis();

        List<QualityDetail> list = jdbcTemplate.query(sql, new Object[]{lastId, limit},
                new BeanPropertyRowMapper<>(QualityDetail.class));

        long end = System.currentTimeMillis();

        System.out.println("MySQL 取出 1 万条数据（23字段）并映射成 Java 对象耗时：" + (end - start) + " ms");
        System.out.println("实际取到：" + list.size() + " 条");
    }


    private String getProcessStage(int i) {
        String[] stages = {"细胞库检定", "中间控制", "放行检测", "稳定性检测", "运输验证"};
        return stages[i % stages.length];
    }

    private String getItemName(int i) {
        String[] items = {"细胞活性", "无菌检测", "支原体检测", "内毒素检测", "细胞纯度",
                "细胞形态", "染色体核型", "端粒酶活性", "表面标志物", "分化潜能检测"};
        return items[i % items.length];
    }

    private String getItemCategory(int i) {
        String[] categories = {"活力", "安全性", "安全性", "安全性", "纯度", "形态", "遗传", "效力", "鉴别", "效力"};
        return categories[i % categories.length];
    }

    private String getStandardValue(int i) {
        if (i % 10 == 0) return "≥95%";
        if (i % 10 == 1) return "阴性";
        if (i % 10 == 2) return "阴性";
        if (i % 10 == 3) return "<0.25 EU/mL";
        return "符合标准";
    }

    private String getTestValue(int i) {
        if (i % 10 == 0) return (95 + i % 5) + "%";
        if (i % 10 == 1) return "阴性";
        if (i % 10 == 2) return "阴性";
        if (i % 10 == 3) return "<0.1 EU/mL";
        return "合格";
    }

    private String getUnit(int i) {
        if (i % 10 == 0) return "%";
        if (i % 10 == 3) return "EU/mL";
        return "";
    }
}