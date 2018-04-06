package by.htp.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import by.htp.library.bean.Book;
import by.htp.library.dao.impl.BookDaoDBImpl;
import by.htp.library.dao.util.DBConnectionHelper;

public class BookReadByTitleTest {
	
	private Connection connection;
	private Book book;
	private List<Book> expectedList;
	private BookDao dao;

	@BeforeClass
	public void initDefaultDBConnection() {
		connection = DBConnectionHelper.connect();
		System.out.println("BeforeClass: connected to DB");
	}

	@BeforeMethod
	public void getExpectedBook() throws SQLException {

		Statement st = connection.createStatement();
		
		st.executeUpdate("INSERT INTO book (title, description, author) VALUES ('testTitle', 'testDescription', 'testAuthor')");
		
		ResultSet rs = st.executeQuery("SELECT * from book WHERE title = 'testTitle'");

		expectedList = new ArrayList<>();

		while (rs.next()) {
			book = new Book();
			book.setId(rs.getInt("book_id"));
			book.setTitle(rs.getString("title"));
			book.setDescription(rs.getString("description"));
			book.setAuthor(rs.getString("author"));
			expectedList.add(book);
		}
		System.out.println("BeforeMethod: expectedList was recieved");
	}

	@BeforeMethod
	public void initDao() {
		dao = new BookDaoDBImpl();
	}

	@Test
	public void testRecievedCorrectBookList() {

		List<Book> actualList = dao.readByTitle("testTitle");

		Assert.assertEquals(actualList, expectedList,
				"The recieved count of books is not equal real count in DB");
		System.out.println("Test: TestRecievedBooksCount");
	}

	@AfterMethod
	public void cleanExpectedValues() throws SQLException {
		book = null;
		expectedList = null;
		Statement st = connection.createStatement();
		st.executeUpdate("DELETE FROM book WHERE title = 'testTitle'");
		System.out.println("AfterMethod: expectedList null value");
	}

	@AfterClass
	public void closeDefaultDBConnection() {
		DBConnectionHelper.disconnect(connection);
		System.out.println("AfterClass: disconnect DB");
	}
}
