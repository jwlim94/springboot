package org.example.springboot.domain.posts;

import org.example.springboot.domain.posts.Posts;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostsTest {

  private Posts posts = Posts.builder().author("James").build();

  @Test
  public void builderTest() {
    assertThat(posts.getAuthor()).isEqualTo("James");
    assertThat(posts.getContent()).isNull();
    assertThat(posts.getTitle()).isNull();
  }
}
