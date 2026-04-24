package com.example.demo;

import com.example.demo.modules.user.entity.QualityReport;
import com.example.demo.modules.user.entity.TestData;
import com.example.demo.modules.user.entity.TestItem;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// SpringBoot测试
@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private MongoTemplate mongoTemplate;
    // 测试方法
    @Test
    void contextLoads() {
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Test
    public void batchInsert10000() {
        // 1. 先建表（如果还没有的话）
        String createTable = "CREATE TABLE IF NOT EXISTS test_data (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(100)," +
                "content TEXT," +
                "status INT," +
                "create_time DATETIME" +
                ")";
        jdbcTemplate.execute(createTable);
        System.out.println("表创建成功");

        // 2. 批量插入1万条数据
        String sql = "INSERT INTO test_data (name, content, status, create_time) VALUES (?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            // 模拟每条数据约2KB
            String content = generateRandomString(1900); // 生成约1900字符的字符串，加上其他字段约2KB
            Object[] args = {
                    "name_" + i,
                    content,
                    i % 5,
                    LocalDateTime.now()
            };
            batchArgs.add(args);
        }

        long start = System.currentTimeMillis();
        jdbcTemplate.batchUpdate(sql, batchArgs);
        long end = System.currentTimeMillis();

        System.out.println("插入1万条数据耗时：" + (end - start) + " ms");
    }
    @Test
    public void insertBatch() {
        // 1. 获取集合（MongoDB 会自动创建）
        MongoCollection<Document> collection = mongoTemplate.getCollection("test_data");

        // 2. 生成一个约 2KB 的固定字符串（所有数据共用，避免循环内重复生成）
        String fixedContent = generateRandomString(1900);  // 1900字符 ≈ 1.9KB，加上其他字段约 2KB

        // 3. 准备 1 万条数据
        List<Document> documents = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            Document doc = new Document();
            doc.append("id", i)
                    .append("name", "name_" + i)
                    .append("content", fixedContent)
                    .append("status", i % 5)
                    .append("create_time", new Date());
            documents.add(doc);
        }

        // 4. 批量插入
        long start = System.currentTimeMillis();
        collection.insertMany(documents);
        long end = System.currentTimeMillis();

        System.out.println("MongoDB 插入 1 万条数据耗时：" + (end - start) + " ms");
        System.out.println("插入条数：" + documents.size());
    }
    // 生成指定长度的随机字符串
    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + (i % 26)));
        }
        return sb.toString();
    }
    @Test
    public void testSelect10000Rows() {
        // 先确认表里有足够的数据（之前插入的1万条）
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM test_data", Long.class);
        System.out.println("当前表中共有：" + count + " 条数据");

        if (count < 10000) {
            System.out.println("数据不足1万条，请先执行批量插入");
            return;
        }

        // 使用主键游标分页，取1万条
        Long lastId = 0L;
        int limit = 10000;

        String sql = "SELECT id, name, content, status, create_time FROM test_data WHERE id > ? LIMIT ?";

        long start = System.currentTimeMillis();

        List<TestData> list = jdbcTemplate.query(
                sql,
                new Object[]{lastId, limit},
                new BeanPropertyRowMapper<>(TestData.class)
        );

        long end = System.currentTimeMillis();

        System.out.println("取1万条数据并映射成对象，耗时：" + (end - start) + " ms");
        System.out.println("实际取到：" + list.size() + " 条数据");

        // 可选：打印第一条数据验证
        if (!list.isEmpty()) {
            TestData first = list.get(0);
            System.out.println("第一条数据示例：id=" + first.getId() + ", name=" + first.getName());
        }
    }

    private String generateFixedString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append('x');
        }
        return sb.toString();
    }
    @Test
    public void testBatchInsert() {
        List<Document> documents = new ArrayList<>();

        for (int i = 1; i <= 10000; i++) {
            Document doc = new Document();

            // 1. 基本信息
            doc.append("id", (long) i)
                    .append("reportNo", "BG" + System.currentTimeMillis() + i)
                    .append("productName", "细胞制剂_" + i)
                    .append("batchNo", "20260422" + String.format("%04d", i))
                    .append("sampleCount", 3)
                    .append("sampleStatus", "完好")
                    .append("testDate", new Date())
                    .append("reportDate", new Date());

            // 2. 检验依据
            doc.append("testStandard", "Q/YD 001-2024")
                    .append("judgmentRule", "所有关键项合格，一般项不合格≤2项");

            // 3. 检验项目（10项）
            List<Document> items = new ArrayList<>();
            String[] itemNames = {"细胞活性", "无菌检测", "支原体检测", "内毒素检测",
                    "细胞纯度", "细胞形态", "染色体核型", "端粒酶活性",
                    "表面标志物", "分化潜能检测"};
            int idx=0;
            for (String itemName : itemNames) {

                Document item = new Document();
                item.append("itemName", itemName)
                        .append("standardRequirement", getStandardByItem(itemName,idx))
                        .append("testResult", getTestResultByItem(itemName,idx))
                        .append("unit", getUnitByItem(itemName))
                        .append("judgment", getJudgmentByItem(itemName,""));
                items.add(item);
            }
            doc.append("testItems", items);

            // 4. 综合判定
            doc.append("finalJudgment", "合格")
                    .append("inspector", "张三")
                    .append("reviewer", "李四")
                    .append("approver", "王五");

            documents.add(doc);
            idx++;
        }

        long start = System.currentTimeMillis();
        mongoTemplate.insert(documents, "quality_report");
        long end = System.currentTimeMillis();

        System.out.println("插入 1 万条质检报告（每条 10 项）耗时：" + (end - start) + " ms");
        System.out.println("插入条数：" + documents.size());
    }
    @Test
    public void testConvertOnly() {
        // 1. 准备 1 万条 Java 对象
        List<QualityReport> javaObjects = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            QualityReport report = new QualityReport();
            report.setId((long) i);
            report.setReportNo("BG" + System.currentTimeMillis() + i);
            report.setProductName("细胞制剂_" + i);
            report.setBatchNo("20260422" + String.format("%04d", i));
            report.setSampleCount(3);
            report.setSampleStatus("完好");
            report.setTestDate(new Date());
            report.setReportDate(new Date());
            report.setTestStandard("Q/YD 001-2024");
            report.setJudgmentRule("所有关键项合格，一般项不合格≤2项");

            // 模拟 10 个检测项目
            List<TestItem> items = new ArrayList<>();
            String[] itemNames = {"细胞活性", "无菌检测", "支原体检测", "内毒素检测",
                    "细胞纯度", "细胞形态", "染色体核型", "端粒酶活性",
                    "表面标志物", "分化潜能检测"};
            int idxq=0;
            for (String itemName : itemNames) {
                TestItem item = new TestItem();
                item.setItemName(itemName);
                item.setStandardRequirement(getStandardByItem(itemName,idxq));
                item.setTestResult(getTestResultByItem(itemName,idxq));
                item.setUnit(getUnitByItem(itemName));
                item.setJudgment("合格");
                items.add(item);
                idxq++;
            }
            report.setTestItems(items);

            report.setFinalJudgment("合格");
            report.setInspector("张三");
            report.setReviewer("李四");
            report.setApprover("王五");

            javaObjects.add(report);
        }

        // 2. 只测试转换：Java 对象 → Document（不涉及数据库）
        long start = System.currentTimeMillis();

        List<Document> documents = new ArrayList<>();
        for (QualityReport report : javaObjects) {
            Document doc = new Document();
            doc.append("id", report.getId())
                    .append("reportNo", report.getReportNo())
                    .append("productName", report.getProductName())
                    .append("batchNo", report.getBatchNo())
                    .append("sampleCount", report.getSampleCount())
                    .append("sampleStatus", report.getSampleStatus())
                    .append("testDate", report.getTestDate())
                    .append("reportDate", report.getReportDate())
                    .append("testStandard", report.getTestStandard())
                    .append("judgmentRule", report.getJudgmentRule());

            // 嵌套结构：检测项目列表
            List<Document> itemDocs = new ArrayList<>();
            for (TestItem item : report.getTestItems()) {
                Document itemDoc = new Document();
                itemDoc.append("itemName", item.getItemName())
                        .append("standardRequirement", item.getStandardRequirement())
                        .append("testResult", item.getTestResult())
                        .append("unit", item.getUnit())
                        .append("judgment", item.getJudgment());
                itemDocs.add(itemDoc);
            }
            doc.append("testItems", itemDocs);

            doc.append("finalJudgment", report.getFinalJudgment())
                    .append("inspector", report.getInspector())
                    .append("reviewer", report.getReviewer())
                    .append("approver", report.getApprover());

            documents.add(doc);
        }

        long end = System.currentTimeMillis();

        System.out.println("1 万条质检报告（含10项）Java对象 → Document 转换耗时：" + (end - start) + " ms");
        System.out.println("转换后文档数量：" + documents.size());
    }
    private String getStandardByItem(String itemName, int index) {
        if (itemName.equals("细胞活性")) {
            return "≥" + (95 + index % 5) + "%";  // 95%、96%、97%... 动态变化
        }
        if (itemName.equals("无菌检测")) {
            return index % 10 == 0 ? "阳性" : "阴性";  // 偶尔出现异常
        }
        if (itemName.equals("内毒素检测")) {
            return "<" + (0.1 + (index % 10) * 0.05) + " EU/mL";
        }
        return "符合标准";
    }

    private String getTestResultByItem(String itemName, int index) {
        if (itemName.equals("细胞活性")) {
            return (95 + index % 5) + "%";
        }
        if (itemName.equals("内毒素检测")) {
            return "<" + (0.05 + (index % 10) * 0.03) + " EU/mL";
        }
        return index % 20 == 0 ? "不合格" : "合格";
    }

    private String getUnitByItem(String itemName) {
        if (itemName.equals("细胞活性")) return "%";
        if (itemName.equals("内毒素检测")) return "EU/mL";
        return "";
    }

    private String getJudgmentByItem(String itemName, String testResult) {
        if (itemName.equals("细胞活性")) {
            // 细胞活性 ≥95% 合格，否则不合格
            int value = Integer.parseInt(testResult.replace("%", ""));
            return value >= 95 ? "合格" : "不合格";
        }
        if (itemName.equals("无菌检测")) {
            return "阴性".equals(testResult) ? "合格" : "不合格";
        }
        if (itemName.equals("内毒素检测")) {
            // 内毒素 <0.25 EU/mL 合格
            String numStr = testResult.replace("<", "").replace(" EU/mL", "");
            double value = Double.parseDouble(numStr);
            return value < 0.25 ? "合格" : "不合格";
        }
        return "合格";
    }


}
