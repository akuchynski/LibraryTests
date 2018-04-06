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

import by.htp.library.bean.Employee;
import by.htp.library.dao.impl.EmployeeDaoDBImpl;
import by.htp.library.dao.util.DBConnectionHelper;

public class EmployeeDaoTest {
	
	private Connection connection;
	private List<Employee> expectedList;
	private EmployeeDao dao;

	@BeforeClass
	public void initDefaultDBConnection() {
		connection = DBConnectionHelper.connect();
		System.out.println("BeforeClass: connected to DB");
	}

	@BeforeMethod
	public void getExpectedList() throws SQLException {

		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("select * from employee");

		expectedList = new ArrayList<>();

		while (rs.next()) {
			expectedList.add(new Employee());
		}
		System.out.println("BeforeMethod: expectedList was recieved");
	}

	@BeforeMethod
	public void initDao() {
		dao = new EmployeeDaoDBImpl();
	}

	@Test
	public void testRecievedCorrectEmployeeCount() {

		List<Employee> actualList = dao.readAll();

		Assert.assertEquals(actualList.size(), expectedList.size(),
				"The recieved count of employees is not equal real count in DB");
		System.out.println("Test: TestRecievedEmployeesCount");
	}

	@AfterMethod
	public void cleanExpectedValues() {
		expectedList = null;
		System.out.println("AfterMethod: expectedList null value");
	}

	@AfterClass
	public void closeDefaultDBConnection() {
		DBConnectionHelper.disconnect(connection);
		System.out.println("AfterClass: disconnect DB");
	}

}
