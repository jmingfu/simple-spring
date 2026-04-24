package com.example.demo;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-22 12:53
 * @date 2026-04-22
 */
@SpringBootTest
public class QualifyTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testInsertNested() {
        // 1. 清空集合，保证每次测试数据干净
        mongoTemplate.dropCollection("quality_report_nested");

        // 2. 准备 1 万条 Document（嵌套结构，内容固定）
        List<Document> documents = new ArrayList<>();

        for (int i = 1; i <= 10000; i++) {
            Document doc = new Document();
            doc.append("id", (long) i)
                    .append("reportNo", "BG202604220001")
                    .append("productName", "细胞制剂")
                    .append("batchNo", "202604220001")
                    .append("sampleCount", 3)
                    .append("sampleStatus", "完好")
                    .append("testDate", new Date())
                    .append("reportDate", new Date())
                    .append("testStandard", "Q/YD 001-2024")
                    .append("judgmentRule", "所有关键项合格，一般项不合格≤2项");

            // 嵌套结构：10 个检测项目（内容固定）
            List<Document> items = new ArrayList<>();
            String[] itemNames = {"细胞活性", "无菌检测", "支原体检测", "内毒素检测",
                    "细胞纯度", "细胞形态", "染色体核型", "端粒酶活性",
                    "表面标志物", "分化潜能检测"};

            for (String itemName : itemNames) {
                Document item = new Document();
                item.append("itemName", itemName)
                        .append("standardRequirement", getStandardByItem(itemName))
                        .append("testResult", getTestResultByItem(itemName))
                        .append("unit", getUnitByItem(itemName))
                        .append("judgment", "合格");
                items.add(item);
            }
            doc.append("testItems", items);

            doc.append("finalJudgment", "合格")
                    .append("inspector", "张三")
                    .append("reviewer", "李四")
                    .append("approver", "王五");

            documents.add(doc);
        }

        // 3. 只测试插入耗时
        long start = System.currentTimeMillis();
        mongoTemplate.insert(documents, "quality_report_nested");
        long end = System.currentTimeMillis();

        System.out.println("插入 1 万条嵌套结构数据（内容固定）耗时：" + (end - start) + " ms");
        System.out.println("插入条数：" + documents.size());
    }

    private String getStandardByItem(String itemName) {
        if (itemName.equals("细胞活性")) return "≥95%";
        if (itemName.equals("无菌检测")) return "阴性";
        if (itemName.equals("支原体检测")) return "阴性";
        if (itemName.equals("内毒素检测")) return "<0.25 EU/mL";
        return "符合标准";
    }

    private String getTestResultByItem(String itemName) {
        if (itemName.equals("细胞活性")) return "97%";
        if (itemName.equals("无菌检测")) return "阴性";
        if (itemName.equals("支原体检测")) return "阴性";
        if (itemName.equals("内毒素检测")) return "<0.1 EU/mL";
        return "合格";
    }

    private String getUnitByItem(String itemName) {
        if (itemName.equals("细胞活性")) return "%";
        if (itemName.equals("内毒素检测")) return "EU/mL";
        return "";
    }
}
