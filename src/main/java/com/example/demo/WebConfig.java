package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允許所有路徑 (e.g., /hello, /api/data, ...)
                .allowedOrigins("https://yo-note.netlify.app", "http://localhost:8080") // ⚠️ 替換成你的前端網域
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法
                .allowedHeaders("*") // 允許所有請求標頭
                .allowCredentials(true) // 如果你需要傳遞 Cookies 或授權資訊
                .maxAge(3600); // CORS 預檢請求的緩存時間
    }
}
