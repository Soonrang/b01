package org.zerock.b01.service;

import org.zerock.b01.dto.BoardDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boarDTO);

    void remove(Long bno);
}
