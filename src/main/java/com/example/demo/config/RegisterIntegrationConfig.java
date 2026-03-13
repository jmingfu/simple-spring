package com.example.demo.config;

import com.example.demo.modules.integration.RegisterMessageFilter;
import com.example.demo.modules.integration.RegisterRabbitMQSendHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;

/**
 * Spring Integration DSL配置
 *
 * @author zhuhuix
 * @date 2020-07-15
 */
@Configuration
public class RegisterIntegrationConfig {

    @Bean
    public IntegrationFlow registerFlow(RegisterRabbitMQSendHandler registerRabbitMQSendHandler
            , RegisterMessageFilter registerMessageFilter) {
        return IntegrationFlows
                // 从registerChannel消息通道获取消息
                .from(MessageChannels.direct("registerChannel"))
                // 过滤
                .filter(registerMessageFilter)
                // 发送邮件
                .handle(registerRabbitMQSendHandler)
                .get();

    }
}
