package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.sun.source.tree.IfTree;

public class UserDAO {
	
	private Connection conn;
	private PreparedStatement pstmt; //SQL injection을 방어하기 위한 수단
	private ResultSet rs;
	
	public UserDAO() {
		try	{
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=Asia/Seoul&useSSL=false";
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	} //하나의 계정에 대해 로그인 시도를 하는 함수 완성 =>login action page가 함수를 사용해 사용자에게 로그인 결과를 알려줌.
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?"; //?자리에 ID를 가져옴
		try {
			pstmt = conn.prepareStatement(SQL); //정해진 문장을 SQL에 삽입하는 인스턴스를 가져옴
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();//result(결과를 담을 수 있는 객체에 실행한 결과를 넣어줌
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword))//입력받은 password가 가지고있는 password와 동일하면 return1(로그인성공 반환)
					return 1;
				else
					return 0;
			}
			return -1;//result가 존재하지 않으면 아이디가 없음을 알려줌(-1)
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -2;  //데이터베이스 오류를 의미
	}
	
	public int join(User user) { //User클래스를 통해 만들어지는 하나의 인스턴스
		String SQL = "INSERT INTO USER VALUES (?,?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

}
