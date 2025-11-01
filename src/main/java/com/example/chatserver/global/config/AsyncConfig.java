package com.example.chatserver.global.config;

import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {

    @Bean(name = "messageTaskExecutor")
    public Executor messageTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 활성 상태를 유지할 스레드 수
        executor.setCorePoolSize(5);

        // 최대 스레드 수
        executor.setMaxPoolSize(10);

        // 큐에 대기 작업 수
        executor.setQueueCapacity(50);

        executor.setRejectedExecutionHandler((runnable, threadPoolExecutor) -> {
            log.warn("메시지 저장 작업 거부됨. 큐가 꽉 찼습니다.");
        });

        executor.initialize();

        log.info("비동기 메시지 실행기 초기화 완료: " + "corePoolSize={}, maxPoolSize={}, queueCapacity={}",
            executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());

        return executor;
    }
}
