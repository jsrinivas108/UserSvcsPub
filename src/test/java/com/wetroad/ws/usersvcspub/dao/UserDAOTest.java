package com.wetroad.ws.usersvcspub.dao;

import java.sql.Timestamp;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wetroad.ws.usersvcspub.dao.UserDAO;
import com.wetroad.ws.usersvcspub.japi.vo.UserVO;

import junit.framework.TestCase;

public class UserDAOTest extends TestCase {

	UserDAO dao = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		
	}
	
	@Override
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		System.out.println("Inside setUp");
		ApplicationContext ac = new ClassPathXmlApplicationContext("dao/db-appCntxt.xml");
		dao = ac.getBean("userDAO", UserDAO.class);
	}

	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
		System.out.println("Inside tearDown");
	}
	
	@Test
	public void testGetUser()
	{
		System.out.println("Inside testGetUser:");
		UserVO user = dao.getUser("");
		System.out.println("user="+user);
		assertNull("User from db is not null", user);
	}

	@Test
	public void testInsertUserMin()
	{
		System.out.println("Inside testInsertUserMin");
		long testStartTime = System.currentTimeMillis();
		UserVO userVO = new UserVO();
		userVO.userPK = UUID.randomUUID().toString();
		userVO.displayName = "DN-"+userVO.userPK;
		dao.insertUser(userVO);
		userVO.createTime = new Timestamp(testStartTime);
		userVO.modifyTime = userVO.createTime;
		assertUser(userVO);
	}

	@Test
	public void testInsertUserMax()
	{
		System.out.println("Inside testInsertUserMax");
		long testStartTime = System.currentTimeMillis();
		UserVO userVO = new UserVO();
		userVO.userPK = UUID.randomUUID().toString();
		userVO.displayName = "DN-"+userVO.userPK;
		userVO.gender = "A";
		userVO.birthYear = 2018;
		userVO.birthMonth = 10;
		dao.insertUser(userVO);
		userVO.createTime = new Timestamp(testStartTime);
		userVO.modifyTime = userVO.createTime;
		assertUser(userVO);
	}

	@Test
	public void testUpdateUser()
	{
		System.out.println("Inside testUpdateUser");
		long testStartTime = System.currentTimeMillis();
		UserVO userVO = new UserVO();
		userVO.userPK = UUID.randomUUID().toString();
		userVO.displayName = "DN-"+userVO.userPK;
		userVO.gender = "A";
		userVO.birthYear = 2018;
		userVO.birthMonth = 10;
		dao.insertUser(userVO);
		userVO.createTime = new Timestamp(testStartTime);
		userVO.modifyTime = userVO.createTime;
		UserVO userVODB = assertUser(userVO);
		
		testStartTime = System.currentTimeMillis();
		userVODB.displayName = "DN2-"+userVO.userPK;
		userVODB.gender = "B";
		userVODB.birthYear = 2018;
		userVODB.birthMonth = 11;
		dao.updateUser(userVODB);
		userVODB.modifyTime = new Timestamp(testStartTime);
		assertUser(userVODB);
	}

	private UserVO assertUser(UserVO userVO) {
		UserVO user = dao.getUser(userVO.userPK);
		System.out.println("user="+user);
		assertNotNull("User from db is null", user);
		assertEquals("UserPK mismatch", userVO.userPK, user.userPK);
		assertEquals("display name mismatch", userVO.displayName, user.displayName);
		assertEquals("gender mismatch", userVO.gender, user.gender);
		assertEquals("birthyear mismatch", userVO.birthYear, user.birthYear);
		assertEquals("birth month mismatch", userVO.birthMonth, user.birthMonth);
		assertNotNull("create time null", user.createTime);
		assertNotNull("modify time null", user.modifyTime);
		assertTrue("create time not auto set. Starttime:"+userVO.createTime.getTime()+" createtime:"+user.createTime.getTime(), 
				(userVO.createTime.getTime() <= user.createTime.getTime()));
		assertTrue("modify time not auto set. Starttime:"+userVO.modifyTime.getTime()+" modifytime:"+user.modifyTime.getTime(), 
				(userVO.modifyTime.getTime() <= user.modifyTime.getTime()));
		return user;
	}

}
