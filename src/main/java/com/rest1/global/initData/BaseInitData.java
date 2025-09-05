package com.rest1.global.initData;

import com.rest1.domain.post.post.entity.Post;
import com.rest1.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;
    private final PostService postService;

    @Bean
    ApplicationRunner initDataRunner() {
        return args -> {
            self.work1();   //프록시를 타야만 트렌젝셔널이 실행됨, 리얼 객체(this)는 안된다.
        };
    }

    @Transactional
    public void work1(){
        if(postService.count() > 0) {
            return;
        }

        Post post1 = postService.write("제목1", "내용1");
        Post post2 = postService.write("제목2", "내용2");
        Post post3 = postService.write("제목3", "내용3");

        post1.addComment("모두 돌고 돌아 제 자릴 찾고");
        post1.addComment("사라졌던 별 다시 또 태어날 때쯤");
        post1.addComment("그때쯤 우리 꼭 만나요");
        post2.addComment("그떄는 꼭 혼자 있어줘요");
        post2.addComment("외워두세요");
    }
}