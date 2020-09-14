package com.naver.client.apicontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.naver.client.common.BaseController;
import com.naver.client.dto.ChatDto;
import com.naver.client.mapper.Chat;
import com.naver.client.resource.BaseResource;
import com.naver.client.resource.CommonResource;
import com.naver.client.vo.ChatUserVo;
import com.naver.client.vo.ChatVo;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/chats")
public class ChatApiController extends BaseController {
	
	/*
	 * chat 만들기
	 */
	@PostMapping
	public ResponseEntity createChat(@RequestBody ChatDto chatDto, @RequestHeader("Authorization") String token) {
		// TODO chatUserId는 @header의 access_token을 해석해서 사용한다.
		int userId = Integer.parseInt(jwtTokenProvider.getUserNameFromJwt(token));
		
		/*
		 * Mapping
		 */
		Chat chat = modelMapper.map(chatDto, Chat.class);
		List<Integer> members = new ArrayList<Integer>(chatDto.getFriendIds());
		members.add(userId);
		

		/*
		 * inset
		 */
		chatService.insert(chat, members);

		/*
		 * createVo
		 */
		ChatVo chatVo = modelMapper.map(chat, ChatVo.class);
		chatVo.setMembers(chatUserService.selectIds(members));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chat", chatVo);

		return ResponseEntity.ok(new CommonResource(OK_CODE, OK, map));
	}

	/*
	 * userId가 참여하는 chat목록을 반환한다.
	 */
	@GetMapping
	public ResponseEntity searchChatList(@RequestHeader("Authorization") String token) {
		// TODO token 검증 작업이 필요하다.
		int userId = Integer.parseInt(jwtTokenProvider.getUserNameFromJwt(token));

		List<ChatVo> chats = chatService.selectChatList(userId);
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("chats", chats);

		return ResponseEntity.ok().body(new CommonResource(OK_CODE, OK, map));

	}

	/*
	 * 채팅방 나가기
	 */
	@DeleteMapping("/{chatId}")
	public ResponseEntity deleteChat(@RequestHeader("Authorization") String token, @PathVariable("chatId") int chatId) {
		BaseResource resource = null;

		// TODO token 검증 작업 필요하다.

		int userId = Integer.parseInt(jwtTokenProvider.getUserNameFromJwt(token));
		/*
		 * delete success
		 */
		if (chatMemberService.delete(chatId, userId)) {
			resource = new BaseResource(OK_CODE, OK);
			return ResponseEntity.ok(resource);
		}
		/*
		 * delete failed
		 */
		else {
			resource = new BaseResource(Bad_Request_CODE, Bad_Request);
			return ResponseEntity.badRequest().body(resource);
		}
	}

	/*
	 * 채팅 내 멤버 조회
	 */
	@GetMapping("/{chatId}/members")
	public ResponseEntity searchChatMember(@RequestHeader("Authorization") String token,
			@PathVariable("chatId") int chatId) {
		int userId = Integer.parseInt(jwtTokenProvider.getUserNameFromJwt(token));

		List<ChatUserVo> chatUserVos = chatService.selectChatMembers(chatId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("members", chatUserVos);

		return ResponseEntity.ok(new CommonResource(OK_CODE, OK, map));

	}

	/*
	 * 채팅 메시지 조회
	 */
	@GetMapping("/{chatId}")
	public ResponseEntity searchMessages(@RequestHeader("Authorization") String token,
			@RequestParam(value = "lastMessageId", required = false, defaultValue = "BASIC") String lastMessageId,
			@RequestParam(value = "size", required = false, defaultValue = "20") String size,
			@PathVariable("chatId")int chatId) {
		int userId = Integer.parseInt(jwtTokenProvider.getUserNameFromJwt(token));
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if("BASIC".equals(lastMessageId)) {
			map.put("messages",chatMessageService.selectMessageVoNoOption(chatId, Integer.parseInt(size)));
		}else {
			map.put("messages", chatMessageService.selectMessageVoLastMessageIdOption(chatId, Integer.parseInt(size), Integer.parseInt(lastMessageId)));
		}
		
		return ResponseEntity.ok(new CommonResource(OK_CODE, OK, map));
	}
	
	/*
	 * 
	 */

}