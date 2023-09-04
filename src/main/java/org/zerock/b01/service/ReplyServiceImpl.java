package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Reply;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.repository.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    private final ModelMapper modelMapper;
    @Override
    public Long register(ReplyDTO replyDTO) {

       Reply reply = modelMapper.map(replyDTO, Reply.class);
       Long rno = replyRepository.save(reply).getRno();
        return rno;
    }

    @Override
    public ReplyDTO read(Long rno) {
        Optional<Reply> replyOptional = replyRepository.findById(rno);
        Reply reply = replyOptional.orElseThrow(); // 만약 댓글이 없으면 예외를 던짐
        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {

        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());
        Reply reply = replyOptional.orElseThrow(); // 만약 댓글이 없으면 예외를 던짐
        reply.changeText(replyDTO.getReplyText()); // 댓글의 내용만 수정
        replyRepository.save(reply);

    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }

    // 댓글서비스 가장 중요한 기능: 특정 게시물의 댓글 목록을 페이징 처리
    // PageRequestDTO를 이용해서 페이지 관련 정보를 처리, ReplyRespository를 통해서 특정 게시물에 속하는
    // Page<Reply>를 구한다. 실제로 반환되어야하는 타입은 Reply가 아니라 ReplyDTO 타입이다.
    // Impl에서 변환작업을 해준다.
    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        // 페이지 정보를 이용하여 Pageable 객체를 생성합니다.
        Pageable pageable = (Pageable) PageRequest.of(
                pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1, // 페이지 번호는 0부터 시작하므로 조정합니다.
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending() // 댓글 번호(Rno)를 기준으로 오름차순 정렬합니다.
        );

        // replyRepository를 사용하여 게시물 번호(bno)에 해당하는 댓글을 페이징하여 조회합니다.
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        // 조회된 댓글을 ReplyDTO로 변환하고, List에 저장합니다.
        List<ReplyDTO> dtoList =
                result.getContent().stream()
                        .map(reply -> modelMapper.map(reply, ReplyDTO.class))
                        .collect(Collectors.toList());

        // PageResponseDTO 객체를 생성하여 결과를 담고 반환합니다.
        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements()) // 전체 댓글 수를 설정합니다.(Long > int)
                .build();
    }

}
