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

import by.htp.library.bean.Book;
import by.htp.library.dao.impl.BookDaoDBImpl;
import by.htp.library.dao.util.DBConnectionHelper;

public class BookReadByIdTest {
	
	private Connection connection;
	private Book expectedBook;
	private BookDao dao;

	@BeforeClass
	public void initDefaultDBConnection() {
		connection = DBConnectionHelper.connect();
		System.out.println("BeforeClass: connected to DB");
	}

	@BeforeMethod
	public void getExpectedBook() throws SQLException {

		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("SELECT * from book WHERE book_id = 1");
		while (rs.next()) {
			expectedBook = new Book();
			expectedBook.setId(rs.getInt("book_id"));
			expectedBook.setTitle(rs.getString("title"));
			expectedBook.setDescription(rs.getString("description"));
			expectedBook.setAuthor(rs.getString("author"));
		}
		System.out.println("BeforeMethod: expectedBook was recieved");
	}

	@BeforeMethod
	public void initDao() {
		dao = new BookDaoDBImpl();
	}

	@Test
	public void testRecievedCorrectBook() {

		Book actualBook = dao.read(1);

		Assert.assertEquals(actualBook, expectedBook,
				"The recieved count of books is not equal real count in DB");
		System.out.println("Test: TestRecievedBooksCount");
	}

	@AfterMethod
	public void cleanExpectedValues() throws SQLException {
		expectedBook = null;
		System.out.println("AfterMethod: expectedBook null value");
	}

	@AfterClass
	public void closeDefaultDBConnection() {
		DBConnectionHelper.disconnect(connection);
		System.out.println("AfterClass: disconnect DB");
	}

}
