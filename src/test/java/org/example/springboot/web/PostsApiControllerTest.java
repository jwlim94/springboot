package org.example.springboot.web;

import org.example.springboot.domain.posts.Posts;
import org.example.springboot.domain.posts.PostsRepository;
import org.example.springboot.web.dto.PostsSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //1
public class PostsApiControllerTest {

  @LocalServerPort //2; 1로부터 자동으로 port 넘버를 받는다?
  private int port;

  @Autowired
  private TestRestTemplate restTemplate; //3; Controller 와 JPA 기능까지 한번에 테스트 할 떄 사용한다.

  @Autowired
  private PostsRepository postsRepository;

  @After
  public void tearDown() throws Exception {
    postsRepository.deleteAll();
  }

  @Test
  public void savePosts() throws Exception {
    //given
    String title = "title for testing posts api";
    String content = "content for testing posts api";
    PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
      .title(title)
      .content(content)
      .author("author for testing posts api")
      .build();

    String url = "http://localhost:" + port + "/api/v1/posts"; //4

    //when
    ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class); //5 =~ mvc.perform

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK); //6
    assertThat(responseEntity.getBody()).isGreaterThan(0L); //7; not sure exactly what it is  comparing to

    List<Posts> posts = postsRepository.findAll();
    assertThat(posts.get(0).getTitle()).isEqualTo(title);
    assertThat(posts.get(0).getContent()).isEqualTo(content);
  }
}
