package org.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing NOTE: HelloControllerTest 에서 WebMvcTest 를 사용하기 위해 @SpringBootApplication 과 @EnableJpaAuditing 을 분리한다.
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
