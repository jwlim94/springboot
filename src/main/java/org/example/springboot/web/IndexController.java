package org.example.springboot.web;

import lombok.RequiredArgsConstructor;
import org.example.springboot.service.posts.PostsService;
import org.example.springboot.web.dto.PostsResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController { // PURPOSE: 페이지간의 이동 API

  private final PostsService postsService;

  @GetMapping("/")
  public String index(Model model) {
    //PURPOSE: Model; 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있다.
    //PURPOSE: 여기서는 postsService.findAllDesc()로 가져온 결과를 posts 로 index.mustache 에 전달한다.
    model.addAttribute("posts", postsService.findAllDesc());
    return "index";
  }

  @GetMapping("/posts/save")
  public String postsSave() {
    return "posts-save";
  }

  @GetMapping("/posts/update/{id}")
  public String postsUpdate(@PathVariable Long id, Model model) {
    PostsResponseDto dto = postsService.findById(id);
    model.addAttribute("post", dto);
    return "posts-update";
  }
}
