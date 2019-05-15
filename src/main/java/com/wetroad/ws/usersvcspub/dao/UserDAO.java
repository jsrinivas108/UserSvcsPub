package com.wetroad.ws.usersvcspub.dao;

import com.wetroad.ws.usersvcspub.japi.vo.UserVO;

public interface UserDAO {

	public void insertUser(UserVO userVO);
	public boolean updateUser(UserVO userVO);
	public UserVO getUser(String userPK);
}
