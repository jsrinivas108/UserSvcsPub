package com.wetroad.ws.usersvcspub.japi;

import com.wetroad.ws.usersvcspub.japi.vo.AppResponse;
import com.wetroad.ws.usersvcspub.japi.vo.UserVO;

public interface UserService {

	public AppResponse<UserVO> createUser(UserVO userVO);
	public AppResponse<UserVO> updateUser(UserVO userVO);
	public AppResponse<UserVO> getUser(String userPK);
}
