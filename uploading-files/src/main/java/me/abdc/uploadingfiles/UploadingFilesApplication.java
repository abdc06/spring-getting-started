package me.abdc.uploadingfiles;

import me.abdc.uploadingfiles.storage.StorageProperties;
import me.abdc.uploadingfiles.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class UploadingFilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadingFilesApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            //서버 구동 초기화 시점에 파일 디렉토리를 삭제, 생성하는 작업
            storageService.deleteAll();
            storageService.init();
        };
    }

}
