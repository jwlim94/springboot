package org.example.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts,Long> { // JpaRepository<Entity 클래스, PK 타입> =~ Dao
  // pair with Posts class; 함께 위치해야 한다.
  // 기본적인 CRUD 메소드가 자동으로 생성된다.
}
