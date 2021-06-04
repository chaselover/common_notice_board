package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO() 
	{
		try	
		{
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=Asia/Seoul&useSSL=false";
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public String getDate() 
	{
		String SQL = "SELECT NOW()";
		try 
		{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) 
			{
				return rs.getString(1);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	public int getNext() 
	{
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC"; //내림차순해서 마지막 글번호 가져옴
		try 
		{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1; //마지막번호 +1해서 그다음 게시글번호
			}
			return 1; //현재 첫번째 게시물인경우 1반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int write(String bbsTitle, String userID, String bbsContent) 
	{
		String SQL = "INSERT INTO BBS VALUES (?,?,?,?,?,?)"; //내림차순해서 마지막 글번호 가져옴
		try 
		{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return -1;
	}	
	
	public ArrayList<Bbs> getList(int pageNumber)
	{
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable =1 ORDER BY bbsID DESC LIMIT 10"; //내림차순해서위에서 10개 가져옴
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try 
		{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber -1)*10);
			rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return list;
	
	}
	
	public boolean nextPage(int pageNumber) { //페이징처리를 위해 존재하는함수( 특정 몇 페이지가 존재하는지 물어봄)
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable =1 ORDER BY bbsID DESC LIMIT 10"; //내림차순해서위에서 10개 가져옴
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber -1)*10);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Bbs getBbs(int bbsID) {
		String SQL = "SELECT * FROM BBS WHERE bbsID = ? "; 
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;  //글 정보를 모두 저장해 함수 호출한 대상에 반환
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; //해당 글 없으면 null반환
		
	}
	
	public int update(int bbsID, String bbsTitle, String bbsContent) { //write 가져왔음
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?"; //특정한 아이디에 해당하는 제목과 내용 바꾸겠다.
		try 
		{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return -1;
	}
	
	public int delete(int bbsID) {
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?"; //글을 삭제해도 글에대한 정보는 남아있게 delete안하고 available만 0으로
		try 
		{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return -1; //데이터베이스오류
	}
	
}
