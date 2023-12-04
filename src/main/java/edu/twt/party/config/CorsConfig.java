package edu.twt.party.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Guohezu
 */

@Configuration
public class CorsConfig {
    private static final String[] ALLOW_ORIGINS = {
            "http://localhost",
            "http://127.0.0.1"
    };
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

//        for (String s: allowOrigins){
//            corsConfiguration.addAllowedOrigin(s);
//        }
        //允许任何域名
        corsConfiguration.addAllowedOriginPattern("*");
        //允许任何头
        corsConfiguration.addAllowedHeader("*");
        //允许任何方法
        corsConfiguration.addAllowedMethod("*");
        //设置预检有效期
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //注册
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}
