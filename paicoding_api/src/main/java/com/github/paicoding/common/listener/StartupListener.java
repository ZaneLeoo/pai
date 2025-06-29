package com.github.paicoding.common.listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
/**
 * @author Zane Leo
 * @date 2025/3/29 12:21
 * 应用启动后的输出文字
 */

@Component
public class StartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 启动时的控制台打印
        System.out.println("-------------------------------");
        System.out.println(" 🎉 PAICODING 应用启动成功啦！ 🚀");
        System.out.println(" 一切准备就绪，Let's go!");
        System.out.println("-------------------------------");
    }
}
