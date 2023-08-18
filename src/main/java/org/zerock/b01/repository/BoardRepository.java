package org.zerock.b01.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.QBoard;
import org.zerock.b01.repository.search.BoardSearch;

// 스프링 데이타 JPA를 이용할 때 JpaRepository라는 인터페이스를 이용해 선언만으로 데이터베이스 관련 작업 처리 가능
// 개발단계에서 JpaRepository 인터페이스 상속하는 인터페이스를 선언하는 거산으로 CRUD 페이징 처리 완료
// JpaRepository 인터페이스 상속할 때 엔티티 타입, @ID타입을 지정해줘야함
public interface BoardRepository extends JpaRepository<QBoard, Long>, BoardSearch {

    @Query(value = "select now()", nativeQuery = true)
    String getTime();
}
