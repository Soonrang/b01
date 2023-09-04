package org.zerock.b01.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "board")
@ToString
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    //다대일 관계로 구성됨을 설명
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String replyText;

    private String replyer;

    // 댓글을 수정하는 경우에는 Reply 객체에서 replyText 만을 수정할 수 있다.
    public void changeText(String text) {
        this.replyText = text;
    }
}
