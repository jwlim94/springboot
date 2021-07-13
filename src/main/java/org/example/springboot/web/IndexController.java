package org.example.springboot.web;

import lombok.RequiredArgsConstructor;
import org.example.springboot.service.posts.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

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
}
