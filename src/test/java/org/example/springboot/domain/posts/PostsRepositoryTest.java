package org.example.springboot.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

  @Autowired
  PostsRepository postsRepository;

  @After
  public void cleanup() {
    postsRepository.deleteAll();
  }

  @Test
  public void saveAndFindPosts() {
    //given
    String title = "title for testing";
    String content = "content for testing";

    postsRepository.save(Posts.builder()
      .title(title)
      .content(content)
      .author("limjongwoo1994@gmail.com")
      .build());

    //when
    List<Posts> postsList = postsRepository.findAll();

    //then
    Posts posts = postsList.get(0);
    assertThat(posts.getTitle()).isEqualTo(title);
    assertThat(posts.getContent()).isEqualTo(content);
  }

  @Test
  public void saveBaseTimeEntity() {
    //given
    LocalDateTime now = LocalDateTime.of(2021, 7, 10, 20, 47, 0);
    postsRepository.save(Posts.builder()
      .title("title for testing Audit")
      .content("content for testing Audit")
      .author("author for testing Audit")
      .build());

    //when
    List<Posts> postsList = postsRepository.findAll();

    //then
    Posts posts = postsList.get(0);

    System.out.println(">>>>>>> createdDate = " + posts.getCreatedDate() + ", modifiedDate = " + posts.getModifiedDate());

    assertThat(posts.getCreatedDate()).isAfter(now);
    assertThat(posts.getModifiedDate()).isAfter(now);
  }
}
