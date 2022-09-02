package com.Oxog.Beta.Controller;

import com.Oxog.Beta.model.Board;
import com.Oxog.Beta.model.User;
import com.Oxog.Beta.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    // db 연동할 repository 객체 생성
    @Autowired
    private BoardRepository boardRepository;

    // 게시판 목록
    @GetMapping("/list")
    public String boardlist(Model model,@PageableDefault(size = 15, sort="id") Pageable pageable,
                            @RequestParam(required = false, defaultValue = "" ) String searchText
                          ){ //@PageableDefault(size = 15) 페이지 디폴트 값
        //페이징 처리
        //   Page<Board> boards = boardRepository.findAll(pageable); // db 에서 findAll 모든 걸 검색
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText,searchText,pageable);
        int startPage = 1;// 현재 페이지 넘버
        int endPage = boards.getTotalPages();




        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("boards",boards);
        return "board/CommunityList";
    }

    @GetMapping("/detail")
    public String boardDetail(@RequestParam Long id , Model model){

        Board board = boardRepository.findById(id).orElse(null);

        model.addAttribute("board",board);

        return "/board/BoardDetail";
    }




    @GetMapping("/form")
    public String boardform(HttpServletRequest request, Model model , @RequestParam(required = false) Long id){ //CommunityList 에 저장된 값 매개로 받기
        HttpSession session = request.getSession();


        if(id == null){

            model.addAttribute("board",new Board());
        }else{
            Board board = boardRepository.findById(id).orElse(null); // 매개로 받은 id로 찾은값 board 에 담기
            model.addAttribute("board",board);
        }


        return "board/BoardForm";
    }

    @PostMapping("/form")
    public String boardformSubmit(@Valid Board board, BindingResult bindingResult, Model model){ //@ModelAttribute Board board 모델에 저장된값 매게로 받기

        //BindingResult 가 entity 에 있는 notnull값을 확인후 유효성 판단
        if(bindingResult.hasErrors()){
        return "board/BoardForm";
        }

        
        //유효성 체크
        if(board.getTitle()==null || board.getTitle()==""){
            return "redirect:/board/list";
        } else if (board.getContent()==null || board.getContent()=="") {
            return "redirect:/board/list";
        }



        boardRepository.save(board);
        return "redirect:/board/list";
    }






}
