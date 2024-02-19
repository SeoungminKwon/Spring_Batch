package com.ll.sbbatch.global.initDate;

import com.ll.sbbatch.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class NotProd {
    @Autowired
    @Lazy //빈 생성을 지연, NotProd인스턴스 생성이 필요한 순간까지 지연됨
    private NotProd self; // 자기 자신의 프록시 인스턴스를 주입받기 위한 목적, 트랜젝션 관리 등에서 자기 참조가 필요할 때 이런식으로 작성
    private final ProductService productService;

    //ApplicationRunner - 스프링 부트 애플리케이션의 시작 시점에 실행되는 코드를 정의화는데 사용됨
    @Bean
    ApplicationRunner initNotProd(){
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1(){
        IntStream.rangeClosed(1, 100).forEach(i -> productService.create("product" + i));
    }
}
