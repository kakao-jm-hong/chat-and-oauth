package com.naver.client.vo;

import java.util.List;

import com.naver.client.mapper.Chat;
import com.naver.client.mapper.LastMessageAndAt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatVo{
	int id;
	String image;
	String name;
	String lastMessage;
	Long lastAt;
	String type;
	int unreadCnt;
	List<ChatUserVo> members;

	public ChatVo(Chat chat, LastMessageAndAt lastMessageAndAt, int unreadCnt, List<ChatUserVo> members) {

		this.id = chat.getId();
		this.image = chat.getImage();
		this.name = chat.getName();
		this.type = chat.getType();
		
		if (lastMessageAndAt != null) {
			this.lastMessage = lastMessageAndAt.getLastMessage();
			this.lastAt = lastMessageAndAt.getLastAt();
		}
		
		this.unreadCnt = unreadCnt;
		this.members = members;
	}

	public void update(LastMessageAndAt lastMessageAndAt, int unreadCnt, List<ChatUserVo> members) {
			if(lastMessageAndAt!=null) {
				this.lastMessage = lastMessageAndAt.getLastMessage();
				this.lastAt = lastMessageAndAt.getLastAt();
			}
			this.unreadCnt = unreadCnt;
			this.members = members;
	}

}