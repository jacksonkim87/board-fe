package com.example.boardfe.service;

import java.io.IOException;

import com.example.boardfe.common.util.PagerInfo;
import com.example.boardfe.model.AppUser;
import com.example.boardfe.model.Board;
import com.example.boardfe.model.ResultMsg;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * BoardService
 */
public interface BoardService {
    public Mono<Integer> selectBoardTotalCount();
	public Flux<Board> boardList(PagerInfo pagerInfo);		
	public Mono<Board> selectBoard(int num); 	
	public Mono<ResultMsg>  deleteBoard(int num);
	public Mono<ResultMsg> insertBoard(Board board,AppUser user)throws IOException; 
	public Mono<ResultMsg> updateBoard(Board checkBoard, AppUser user);	
	public Mono<String> ver(String name); 	
	
    
}