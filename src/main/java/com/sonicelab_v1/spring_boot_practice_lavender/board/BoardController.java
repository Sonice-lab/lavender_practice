package com.sonicelab_v1.spring_boot_practice_lavender.board;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardNativeRepository boardNativeRepository;

    // GET http://localhost:8080/  ,   http://localhost:8080/board/list
    @GetMapping({"/", "/board/list"})
    public String list(Model model) {
        List<Board> boardList = boardNativeRepository.findAll();
        model.addAttribute("boardList", boardList);
        return "board/list";
    }

    // GET http://localhost:8080/board/3
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model) {
        Board board = boardNativeRepository.findById(id);
        if (board == null) {
            return "redirect:/";
        }
        model.addAttribute("board", board);
        return "board/detail";
    }

    // GET http://localhost:8080/board/save (화면 요청)
    @GetMapping("/board/save")
    public String saveForm() {
        return "board/save-form";
    }

    // 2. 실제 데이터 저장 처리 (새로 추가해야 할 코드)
// HTML의 <form action="/board/save" method="post"> 와 매칭됩니다.
    @PostMapping("/board/save")
    public String save(@RequestParam(name = "title") String title,
                       @RequestParam(name = "content") String content,
                       @RequestParam(name="username") String username)

    {

        // (이곳에 DB에 데이터를 저장하는 repository 호출 로직 작성)
        boardNativeRepository.save(title, content, username);

        // 저장이 완료되면 게시글 목록 화면이나 메인 화면으로 리다이렉트 (PRG 패턴)
        return "redirect:/";
    }


    // TODO
    // 뼈대용 임시 게시글 한개 (데이터 베이스 연결시 삭제 예정)
    private Map<String, Object> sampleBoard(Long id) {
        return Map.of("id", id,
                "title", id + " 번째 글",
                "content", " 임시 내용....",
                "username", "김민수");
    }

    // 게시글 삭제
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Long id) {
        boardNativeRepository.deleteById(id);
        // PRG 패턴
        return "redirect:/";
    }

    // GET http://localhost:8080/board/1/update (화면 요청)
    @GetMapping("/board/{id}/update")
    public String updateForm(@PathVariable Long id, Model model) {
        // 수정하기 화면 요청 (먼저 조회 부터)
        Board board = boardNativeRepository.findById(id);
        model.addAttribute("board", board);
        return "board/update-form";
    }

    // POST http://localhost:8080/board/1/update (게시글 수정 기능 요청)
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Long id,
                         @RequestParam(name = "title") String title,
                         @RequestParam(name = "content") String content) {

        boardNativeRepository.updateById(title, content, id);
        // PRG 패턴
        return "redirect:/board/" + id; // 리다이렉트 수정된 게시글 상세보기 화면 이동
    }


}
