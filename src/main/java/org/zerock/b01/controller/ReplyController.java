package org.zerock.b01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.service.ReplyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor //의존성 주입을 위해(ReplyService를 주입받도록 설계)
public class ReplyController {

    private final ReplyService replyService;

    // 이전과 달라진점 : ReplyDTO 수집할 때 @Valid적용
    // BindingResult를 파라미터로 추가하고 무네 있을 때는 BindException 을 Throw하도록 수정
    // 메소드 선언부에 BindException을 throw 하도록 수정
    // 메소드 리턴값에 문제가 잆다면 @RestControllerAdvice가 처리할 것이므로 정상적인 결과만 리턴
    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글등록")
    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> register(
            @Valid @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult)throws BindException{

        log.info(replyDTO);

        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        Map<String,Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);

        resultMap.put("rno",rno);

        return resultMap;
    }


//    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")
//    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
//    // RequestBody: JSON 문자열을 ReplyDTO로 변환
//    public ResponseEntity<Map<String,Long>> register(@RequestBody ReplyDTO replyDTO) {
//        log.info(replyDTO);
//        Map<String, Long> resultMap = Map.of("rno",111L);
//        return ResponseEntity.ok(resultMap);
//    }


    @ApiOperation(value = "Replies of Board", notes = "GET방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO){
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);
        return responseDTO;
    }

    @ApiOperation(value = "Read Reply", notes = "GET방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO( @PathVariable("rno")Long rno){
        ReplyDTO replyDTO = replyService.read(rno);
        return replyDTO;
    }

    @ApiOperation(value = "Delete Reply", notes = "DELETE방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String, Long> remove (@PathVariable("rno")Long rno) {

        replyService.remove((rno));

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);

        return resultMap;
    }

    @ApiOperation(value = "Modify Reply", notes = "PUT방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> remove(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO){
        replyDTO.setRno(rno); // 번호를 일치시킴
        replyService.modify(replyDTO);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);
        return resultMap;
    }
}
