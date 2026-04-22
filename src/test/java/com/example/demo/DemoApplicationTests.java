package com.example.demo;

import com.example.demo.modules.user.entity.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// SpringBoot测试
@SpringBootTest
class DemoApplicationTests {

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
}
