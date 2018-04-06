package by.htp.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import by.htp.library.dao.impl.BookDaoDBImpl;
import by.htp.library.dao.util.DBConnectionHelper;

public class BookDeleteTest {
	
	private Connection connection;
	private BookDao dao;

	@BeforeClass
	public void initDefaultDBConnection() {
		connection = DBConnectionHelper.connect();
		System.out.println("BeforeClass: connected to DB");
	}

	@BeforeMethod
	public void getExpectedBook() throws SQLException {

		Statement st = connection.createStatement();
		st.executeUpdate("INSERT INTO book VALUES (99, 'testTitle', 'testDescription', 'testAuthor')");
		System.out.println("BeforeMethod: expectedBook was recieved");
	}

	@BeforeMethod
	public void initDao() {
		dao = new BookDaoDBImpl();
	}

	@Test
	public void testDeletedCorrectBook() throws SQLException {

		dao.delete(99);
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("SELECT * from book WHERE book_id = 99");
		Assert.assertFalse(rs.next(), "The received book is not deleted from DB");
		System.out.println("Test: TestDeletedBook");
		
	}

	@AfterMethod
	public void cleanExpectedValues() throws SQLException {	
		Statement st = connection.createStatement();
		st.executeUpdate("DELETE FROM book WHERE book_id = 99");
		System.out.println("AfterMethod: expectedBook null value");
	}

	@AfterClass
	public void closeDefaultDBConnection() {
		DBConnectionHelper.disconnect(connection);
		System.out.println("AfterClass: disconnect DB");
	}
}
