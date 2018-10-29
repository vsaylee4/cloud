package com.sjsu.cloud.proj;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.sjsu.cloud.proj.repository.impl.NativeDBRepositoryImpl;
import com.sjsu.cloud.proj.service.impl.S3ServiceImpl;

@Configuration
public class AppConfig {
    @Bean
    public NativeDBRepositoryImpl transferService() {
        return new NativeDBRepositoryImpl();
    }
    
    
    @Bean
    public S3ServiceImpl s3ServiceImpl(){
       return new S3ServiceImpl();
    }
}