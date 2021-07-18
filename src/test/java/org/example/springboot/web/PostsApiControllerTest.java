package org.example.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springboot.domain.posts.Posts;
import org.example.springboot.domain.posts.PostsRepository;
import org.example.springboot.web.dto.PostsSaveRequestDto;
import org.example.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //1
public class PostsApiControllerTest {

  @LocalServerPort //2; 1로부터 자동으로 port 넘버를 받는다?
  private int port;

  @Autowired
  private TestRestTemplate restTemplate; //3; Controller 와 JPA 기능까지 한번에 테스트 할 떄 사용한다.

  @Autowired
  private PostsRepository postsRepository;

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  @Before
  public void setup() {
    mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
  }

  @After
  public void tearDown() throws Exception {
    postsRepository.deleteAll();
  }

  @Test
  @WithMockUser(roles="USER")
  public void savePosts() throws Exception {
    //given
    String title = "title for testing save api";
    String content = "content for testing save api";
    PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
      .title(title)
      .content(content)
      .author("author for testing save api")
      .build();

    String url = "http://localhost:" + port + "/api/v1/posts"; //4

    /* NOTE: @WithMockUser(roles="USER")가 MockMvc 에서만 작동함으로 더이상 restTemplate 으로 사용하지 않는다.
    //when
    ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class); //5 =~ mvc.perform

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK); //6
    assertThat(responseEntity.getBody()).isGreaterThan(0L); //7; not sure exactly what it is  comparing to

    List<Posts> posts = postsRepository.findAll();
    assertThat(posts.get(0).getTitle()).isEqualTo(title);
    assertThat(posts.get(0).getContent()).isEqualTo(content);
    */

    //when
    mvc.perform(post(url)
                  .contentType(MediaType.APPLICATION_JSON_UTF8)
                  .content(new ObjectMapper().writeValueAsString(requestDto)))
                  .andExpect(status().isOk());

    //then
    List<Posts> posts = postsRepository.findAll();
    assertThat(posts.get(0).getTitle()).isEqualTo(title);
    assertThat(posts.get(0).getContent()).isEqualTo(content);
  }

  @Test
  @WithMockUser(roles="USER")
  public void updatePosts() throws Exception {
    //given
    Posts savedPosts = postsRepository.save(Posts.builder()  //entity
      .title("title for testing update api")
      .content("content for testing update api")
      .author("author for testing update api")
      .build());

    Long updateId = savedPosts.getId();
    String expectedTitle = "updated title";
    String expectedContent = "updated content";

    PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
      .title(expectedTitle)
      .content(expectedContent)
      .build();

    String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

    HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto); //1

    /* NOTE: @WithMockUser(roles="USER")가 MockMvc 에서만 작동함으로 더이상 restTemplate 으로 사용하지 않는다.
    //when
    ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class); //2

    //then
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isGreaterThan(0L);

    List<Posts> posts = postsRepository.findAll();
    assertThat(posts.get(0).getTitle()).isEqualTo(expectedTitle);
    assertThat(posts.get(0).getContent()).isEqualTo(expectedContent);
    */

    //when
    mvc.perform(put(url)
                  .contentType(MediaType.APPLICATION_JSON_UTF8)
                  .content(new ObjectMapper().writeValueAsString(requestDto)))
                  .andExpect(status().isOk());

    //then
    List<Posts> posts = postsRepository.findAll();
    assertThat(posts.get(0).getTitle()).isEqualTo(expectedTitle);
    assertThat(posts.get(0).getContent()).isEqualTo(expectedContent);
  }
}
