package com.rest1.domain.post.post.controller;

import com.rest1.domain.post.post.dto.PostDto;
import com.rest1.domain.post.post.entity.Post;
import com.rest1.domain.post.post.service.PostService;
import com.rest1.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiV1PostController {

    private final PostService postService;


    // entity, DB구조가 그대로 프론트에 넘어가는건 좋은게 아님
    @GetMapping
    @Transactional(readOnly = true)
    public List<PostDto> list() {
        return postService.findAll().stream()
                .map(PostDto::new)
                .toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PostDto getItem(
            @PathVariable Long id
    ) {
        Post post = postService.findById(id).get();
        return new PostDto(post);
    }

    @DeleteMapping("/{id}")
    public RsData<Void> deleteItem(
            @PathVariable Long id
    ) {
        Post post = postService.findById(id).get();
        postService.delete(post);

        return new RsData<Void>(
                "204-1",
                "%d번 게시물이 삭제되었습니다.".formatted(id)
        );
    }

    record PostWriteForm(
            @NotBlank
            @Size(min = 2, max = 100)
            String title,
            @NotBlank
            @Size(min = 2, max = 100)
            String content
    ) {}

    @PostMapping
    public ResponseEntity<RsData<PostDto>> createItem(
            @RequestBody @Valid PostWriteForm form // JSON으로 받음 @RequestBody가 인식
    ) {
        Post post = postService.write(form.title, form.content);
        RsData<PostDto> rsData = new RsData<>(
                "201-1",
                "%d번게시물이 생성되었습니다.".formatted(post.getId()),
                new PostDto(post)
        );

        return ResponseEntity.status(rsData.getStatusCode()).body(rsData);
    }
}
