package com.naver.client.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.client.mapper.ChatMember;
import com.naver.client.repo.ChatMemberRepo;
import com.naver.client.repo.ChatRepo;

@Service
public class ChatMemberServiceImpl implements ChatMemberService{
	
	@Autowired
	ChatMemberRepo chatMemberRepo;
	
	@Autowired
	ChatRepo chatRepo;
	
	
	@Override
	public boolean insert(int chatId, int userId,int act) {
		return chatMemberRepo.insert(chatId, userId,act);
	}

	@Override
	public boolean delete(int chatId, int userId) {
		
		if("PRIVATE".equals(chatRepo.selectType(chatId))) {
			/*
			 * 1:1 채팅방의 경우 비활성화
			 */
			return chatMemberRepo.updateAct(chatId, userId, 0);
		}
		
		return chatMemberRepo.delete(chatId, userId);
	}

	@Override
	public boolean updateReadTime(ChatMember chatMember) {
		return chatMemberRepo.updateReadTime(chatMember);
	}

	@Override
	public List<Integer> selectChatUserIds(int userId) {
		return chatMemberRepo.selectChatUserIds(userId);
	}

	@Override
	public boolean updateAct(int chatId, int userId, int act) {
		return chatMemberRepo.updateAct(chatId, userId, act);
	}

	@Override
	public int selectChatMemebersCnt(String chatId) {
		return chatMemberRepo.selectChatMemebersCnt(chatId);
	}

	@Override
	public boolean updateReadId(int chatId, int userId) {
		return chatMemberRepo.updateReadId(chatId,userId);
	}

	@Override
	public boolean updateReadId(int chatId, int userId, int lastreadId) {
		return chatMemberRepo.updateReadIdWithValue(chatId, userId, lastreadId);
	}

}
