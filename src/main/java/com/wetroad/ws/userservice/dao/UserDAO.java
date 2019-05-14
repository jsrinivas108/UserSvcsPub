package com.wetroad.ws.userservice.dao;

import com.wetroad.ws.userservice.japi.vo.UserVO;

public interface UserDAO {

	public void insertUser(UserVO userVO);
	public boolean updateUser(UserVO userVO);
	public UserVO getUser(String userPK);
}
