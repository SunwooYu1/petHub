package ezen.ams;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.net.aso.a;

public class JdbcAccountRepository implements AccountRepository {
	
	// DB 연결정보 상수
	private static final String driver = "oracle.jdbc.driver.OracleDriver";
	private static final String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String user = "hr";
	private static final String password = "hr";
	
	private Connection con;
	
	public JdbcAccountRepository() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		con = DriverManager.getConnection(url, user, password);
	}
		
	@Override
	public int getCount() {
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT COUNT(*) cnt")
			  .append(" FROM accounts");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)    rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {}
		}
		return count;
	}

	@Override
	public void addAccount(Account account) throws IOException {
		PreparedStatement pstmt = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" INSERT INTO accounts")
			  .append(" VALUES(?, ?, ?, ?, ?)");
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, account.getAccountNumber());
			pstmt.setString(2, account.getAccountOwner());
			pstmt.setInt(3, account.getPassword());
			pstmt.setLong(4, account.getRestMoney());
			if(account instanceof MinusAccount) {
				MinusAccount ma = (MinusAccount)account;
				pstmt.setLong(5, ma.getBorrowMoney());
			}else {
				pstmt.setLong(5, 0);
			}
			int count = pstmt.executeUpdate();
			System.out.println(count + "계좌 등록 완료...");			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {}
		}
	}

	@Override
	public List<Account> getAccounts() throws IOException {
		List<Account> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT account_num, owner_name, passwd, rest_money, borrow_money")
		  .append(" FROM accounts")
		  .append(" ORDER BY account_num");
		
		try {
			
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			while(rs.next()) {
//				String accountNum = rs.getString("account_num");
//				String accountOwner = rs.getString("owner_name");
//				int passwd = rs.getInt("passwd");
//				long restMoney = rs.getLong("rest_money");
//				long borrowMoney = rs.getLong("borrow_money");
//				
//				Account account = new Account();
//				if(borrowMoney != 0) {
//					account = new MinusAccount();
//					((MinusAccount)account).setBorrowMoney(borrowMoney);
//				}
//				account.setAccountNumber(accountNum);
//				account.setAccountOwner(accountOwner);
//				account.setPassword(passwd);
//				account.setRestMoney(restMoney);
				Account account = makeAccount(rs);
				list.add(account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)    rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {}
		}
		return list;
	}
	
	private Account makeAccount(ResultSet rs) throws SQLException{
		String accountNum = rs.getString("account_num");
		String accountOwner = rs.getString("owner_name");
		int passwd = rs.getInt("passwd");
		long restMoney = rs.getLong("rest_money");
		long borrowMoney = rs.getLong("borrow_money");
		
		Account account = new Account();
		if(borrowMoney != 0) {
			account = new MinusAccount();
			((MinusAccount)account).setBorrowMoney(borrowMoney);
		}
		account.setAccountNumber(accountNum);
		account.setAccountOwner(accountOwner);
		account.setPassword(passwd);
		account.setRestMoney(restMoney);
		return account;
	}

	@Override
	public Account findByNumber(String number) throws IOException {
		Account account = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT account_num, owner_name, passwd, rest_money, borrow_money")
		  .append(" FROM accounts")
		  .append(" WHERE account_num = ?");
		
		try {
			
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, number);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				account = makeAccount(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)    rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {}
		}
		return account;
	}

	@Override
	public List<Account> findByName(String name) throws IOException {
		List<Account> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT account_num, owner_name, passwd, rest_money, borrow_money")
		  .append(" FROM accounts")
		  .append(" WHERE owner_name LIKE ?");
		
		try {
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, "%"+name+"%");
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Account account = makeAccount(rs);
				list.add(account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)    rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {}
		}
		return list;
	}

	@Override
	public boolean removeAccount(String number) throws IOException {
		PreparedStatement pstmt = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" DELETE FROM accounts")
			  .append(" WHERE account_num = ?");
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, number);
			int count = pstmt.executeUpdate();
			if(count != 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {}
		}
		return false;
	}
}
