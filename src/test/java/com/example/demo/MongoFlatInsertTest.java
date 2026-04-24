package com.example.demo;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class MongoFlatInsertTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testFlatInsert() {
        // 1. 清空集合
        mongoTemplate.dropCollection("quality_detail_flat");

        // 2. 准备 1 万条扁平数据（23个字段，单条约0.7KB）
        List<Document> documents = new ArrayList<>();

        for (int i = 1; i <= 40000; i++) {
            Document doc = new Document();
            // 标识字段
            doc.append("id", (long) i)
                    .append("report_id", "RPT" + String.format("%08d", i))
                    .append("batch_no", "B" + System.currentTimeMillis() + i)
                    .append("process_stage", getProcessStage(i));

            // 检测项目字段
            doc.append("item_name", getItemName(i))
                    .append("item_category", getItemCategory(i))
                    .append("standard_value", getStandardValue(i))
                    .append("test_value", getTestValue(i))
                    .append("unit", getUnit(i))
                    .append("result", i % 5 == 0 ? "不合格" : "合格");

            // 方法字段
            doc.append("test_method", "流式细胞术")
                    .append("method_version", "中国药典2025版");

            // 时间与人员
            doc.append("test_date", new Date())
                    .append("operator", "质检员_" + (i % 10))
                    .append("reviewer", "复核员_" + (i % 5))
                    .append("approver", "批准员");

            // 设备与试剂
            doc.append("equipment_id", "设备_" + (i % 20))
                    .append("reagent_lot", "试剂批号_" + i);

            // 审计字段
            doc.append("create_time", new Date())
                    .append("update_time", new Date())
                    .append("remark", "测试数据_" + i);

            documents.add(doc);
        }

        // 3. 只测试插入耗时
        long start = System.currentTimeMillis();
        mongoTemplate.insert(documents, "quality_detail_flat");
        long end = System.currentTimeMillis();

        System.out.println("扁平结构（23字段）插入 1 万条数据耗时：" + (end - start) + " ms");
        System.out.println("插入条数：" + documents.size());
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