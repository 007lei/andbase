package com.andbase.friend;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Relations;
import com.ab.db.orm.annotation.Table;
import com.andbase.model.User;

@Table(name = "chat_message")
public class ChatMsg {
	
	//ID
	@Id
	@Column(name = "_id")
	private int _id;
	
	//�û�ID
	@Column(name = "u_id")
	private String uId;
	
	//�û�ID
	@Column(name = "face_u_id")
	private String faceUid;
	
	//����
	@Column(name = "title")
	private String title;
	
	//����
	@Column(name = "content")
	private String content;
	
	// ����ʱ��
	@Column(name = "create_time")
	private String createTime;
	
	//ͼƬ������Ƶ��ַ
	private String mediaUrl;
	
	//����״̬1�ѷ���/�ѵ����1����ʧ��  0���ڷ���
	@Column(name = "send_state")
	private int sendState;
	
	@Relations(name="user",type="one2one",foreignKey = "u_id",action="query")
	private User user;


	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getSendState() {
		return sendState;
	}

	public void setSendState(int sendState) {
		this.sendState = sendState;
	}

	public String getFaceUid() {
		return faceUid;
	}

	public void setFaceUid(String faceUid) {
		this.faceUid = faceUid;
	}
	
	

}
