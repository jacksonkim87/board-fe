package com.example.boardfe.service;

import java.io.IOException;
import java.time.LocalDateTime;

import com.example.boardfe.common.util.PagerInfo;
import com.example.boardfe.model.AppUser;
import com.example.boardfe.model.Board;
import com.example.boardfe.model.ResultMsg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * BoardServiceImpl
 */
@Service
@Slf4j
public class BoardServiceImpl implements BoardService {
	private static String SUCCESS = "Y";

	@Autowired
	private WebClient client;
	private String apiUrl;

	@Bean
	public WebClient getWebClient() {
		return WebClient.builder().baseUrl(this.apiUrl)				
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	public BoardServiceImpl(@Value("${api.board.url}") final String apiUrl) {
		this.apiUrl = apiUrl;
		getWebClient();
	}

	@Override
	public Mono<Integer> selectBoardTotalCount() {
		return client
		.get()
		.uri("/totalCount")
		.retrieve()
		.bodyToMono(Integer.class);
	}

	@Override
	public Flux<Board> boardList(PagerInfo pagerInfo) {

		int pageSize = 10;

		selectBoardTotalCount().subscribe(count -> {
			pagerInfo.init(pageSize, count);
		});
		String page = String.valueOf(pagerInfo.getPage());

		return client
		.get()			
		.uri(uriBuiler -> uriBuiler.path("")
			.queryParam("size", pageSize)
			.queryParam("page", page)
			.build())
		.retrieve()
		.bodyToFlux(Board.class);

	}

	@Override
	public Mono<Board> selectBoard(int num) {
		return client
		.get()
		.uri("/{num}", num)
		.retrieve()
		.bodyToMono(Board.class);
	}

	@Override
	public Mono<ResultMsg> deleteBoard(int num) {
		return client
		.delete()
		.uri("/{num}", num)				
		.exchange()
		.flatMap(res -> res.toEntity(ResultMsg.class))
		.map(entity -> {
					return getReturnResultMsg(entity);
			});
	}

	@Override
	public Mono<ResultMsg> insertBoard(Board board, AppUser user) throws IOException {
		board.setWriteId(user.getId());
		board.setWriteName(user.getMemberName());
		board.setWriteDate(LocalDateTime.now());
		return client
		.post()
		.body(Mono.just(board), Board.class)
		.exchange()
		.flatMap(res -> res.toEntity(ResultMsg.class)).map(entity -> {
					return getReturnResultMsg(entity);
			});
	}

	@Override
	public Mono<ResultMsg> updateBoard(Board board, AppUser user) {
		Mono<Board> dbBoard = selectBoard(board.getNum()).map(b -> {
			b.setTitle(board.getTitle());
			b.setContents(board.getContents());
			b.setModifyId(user.getId());
			b.setModifyName(user.getMemberName());
			b.setModifyDate(LocalDateTime.now());
			return b;
		});
		return client
		.put()
		.body(dbBoard, Board.class)
		.exchange()
		.flatMap(res -> res.toEntity(ResultMsg.class))
			.map(entity -> {
				return getReturnResultMsg(entity);
			});

	}

	private ResultMsg getReturnResultMsg(ResponseEntity<ResultMsg> entity) {
		ResultMsg resultMsg = entity.getBody();
		if ((HttpStatus.OK).equals(entity.getStatusCode())) {
			resultMsg.setSuccessYn(SUCCESS);
			return resultMsg;
		}
		return resultMsg;

	}

	@Override
	public Mono<String> ver(String name) {

		return client
		.get()
		.uri("/version")
		.header("end-user", name)
		.exchange()
		.flatMap(res -> res.toEntity(String.class))
			.map(entity -> {
				String str = entity.getBody();
				return str;
			});
	}
}