package com.example.boardfe.controller;

import java.io.IOException;

import com.example.boardfe.common.util.PagerInfo;
import com.example.boardfe.model.AppUser;
import com.example.boardfe.model.Board;
import com.example.boardfe.model.ResultMsg;
import com.example.boardfe.service.BoardService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * BoardFeController
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class BoardFeController {
    private final BoardService boardService;

	@GetMapping({"/","/list"})
	public String boardList(PagerInfo pagerInfo, Model model) {        
		Flux<Board> boardList = boardService.boardList(pagerInfo);
		model.addAttribute("boardList", boardList);
		model.addAttribute("pagerInfo", pagerInfo);

		return "board/list";

	}

	@PostMapping("/ajaxList")
	public String boardAjaxList(PagerInfo pagerInfo, Model model) {
		Flux<Board> boardList = boardService.boardList(pagerInfo);

		model.addAttribute("boardList", boardList);
		model.addAttribute("pagerInfo", pagerInfo);

		return "board/list-dataList";

	}

	@GetMapping("/view")
	public String view(@RequestParam int num, Model model) {
		Mono<Board> board = boardService.selectBoard(num);
		model.addAttribute("board", board);

		return "board/view";
	}

	@GetMapping("/write")
	public String writeForm() {
		return "board/form";
	}

	 
	@PostMapping("/write/submit")
	public Mono<String> writeSubmit(@ModelAttribute Board board) throws IOException {
		AppUser user = new AppUser();
		user.setId("user1");
		user.setMemberName("사용자1");
		return boardService.insertBoard(board, user)
			.map(msg -> {
				if (StringUtils.equals(msg.getSuccessYn(), "Y")) {
					return "redirect:/list";
				}
				return "redirect:/write";	
			});
	}

	@GetMapping("/modifyForm")
	public String modifyForm(@RequestParam int num, Model model) {
		Mono<Board> board = boardService.selectBoard(num);
		model.addAttribute("board", board);
		return "board/form";
	}

	@PostMapping("/modify/submit")
	public Mono<String> modify(@ModelAttribute Board board) {
		AppUser user = new AppUser();
		user.setId("user1");
		user.setMemberName("사용자1");

		return boardService.updateBoard(board, user)
			.map(msg -> {
				if (StringUtils.equals(msg.getSuccessYn(), "Y")) {
					return "redirect:/list";
				}		
				return "redirect:/view?num=" + board.getNum();
			});

	}

	@GetMapping("/delete")
	public Mono<String> delete(@RequestParam int num) {
		AppUser user = new AppUser();
		user.setId("user1");
		
		return boardService.selectBoard(num)
			.flatMap(b -> {
				if(StringUtils.equals(b.getWriteId(), user.getId())){
					log.info("======delete======");
					return boardService.deleteBoard(num);
				};		
				log.info("======not delete   ======");	 
				return Mono.just(new ResultMsg("N"));
			})
			.map(msg ->{
				if ( StringUtils.equals(msg.getSuccessYn(), "Y")) {
					return "redirect:/list";
				}
				return "redirect:/list";
			});
	}

	@GetMapping("/rest/list")
	@ResponseBody
	public Flux<Board> rest(PagerInfo pagerInfo) {
		return boardService.boardList(pagerInfo);

	}
	@GetMapping("/ver")
	@ResponseBody
	public Mono<String> ver(@RequestParam String name) {
		return boardService.ver(name);

	}


    
}