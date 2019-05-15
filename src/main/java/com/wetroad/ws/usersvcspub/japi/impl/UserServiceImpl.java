package com.wetroad.ws.usersvcspub.japi.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wetroad.ws.usersvcspub.dao.UserDAO;
import com.wetroad.ws.usersvcspub.japi.UserService;
import com.wetroad.ws.usersvcspub.japi.vo.AppResponse;
import com.wetroad.ws.usersvcspub.japi.vo.AppResponse.Status;
import com.wetroad.ws.usersvcspub.japi.vo.UserVO;

public class UserServiceImpl implements UserService {

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserDAO userDAO = null;
	
	
	public UserDAO getUserDAO()
	{
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO)
	{
		this.userDAO = userDAO;
	}

	@Override
	public AppResponse<UserVO> createUser(UserVO userVO) {
		AppResponse<UserVO> ret = new AppResponse<UserVO>();

		//TODO: need to add validation rules
		userDAO.insertUser(userVO);
		
		//TODO: need to get PK back from db ?
		
		ret.object = userVO;
		ret.status = Status.SUCCESS;
		return ret;
	}

	@Override
	public AppResponse<UserVO> updateUser(UserVO userVO) {
		AppResponse<UserVO> ret = new AppResponse<UserVO>();

		//TODO: need to add validation rules
		userDAO.updateUser(userVO);
		
		ret.object = userVO;
		ret.status = Status.SUCCESS;
		return ret;
	}

	@Override
	public AppResponse<UserVO> getUser(String userPK) {
		AppResponse<UserVO> ret = new AppResponse<UserVO>();

		//TODO: need to add validation rules
		ret.object = userDAO.getUser(userPK);
		ret.status = Status.SUCCESS;
		return ret;
	}

}
