package com.hindfundsbank.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
          .addMapping("/api/**")                            // apply to all your API endpoints
          .allowedOrigins("http://localhost:3000")          // your React app origin
          .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // include OPTIONS for preflight :contentReference[oaicite:0]{index=0}
          .allowedHeaders("*")                              // allow all headers (Authorization, Content-Type, etc.) :contentReference[oaicite:1]{index=1}
          .exposedHeaders("Authorization")                  // expose JWT header back if you set it in responses :contentReference[oaicite:2]{index=2}
          .allowCredentials(true)
          .maxAge(3600);                                    // cache preflight for 1h
    }
}
