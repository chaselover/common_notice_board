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
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC"; //���������ؼ� ������ �۹�ȣ ������
		try 
		{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1; //��������ȣ +1�ؼ� �״��� �Խñ۹�ȣ
			}
			return 1; //���� ù��° �Խù��ΰ�� 1��ȯ
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int write(String bbsTitle, String userID, String bbsContent) 
	{
		String SQL = "INSERT INTO BBS VALUES (?,?,?,?,?,?)"; //���������ؼ� ������ �۹�ȣ ������
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
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable =1 ORDER BY bbsID DESC LIMIT 10"; //���������ؼ������� 10�� ������
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
	
	public boolean nextPage(int pageNumber) { //����¡ó���� ���� �����ϴ��Լ�( Ư�� �� �������� �����ϴ��� ���)
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable =1 ORDER BY bbsID DESC LIMIT 10"; //���������ؼ������� 10�� ������
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
				return bbs;  //�� ������ ��� ������ �Լ� ȣ���� ��� ��ȯ
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; //�ش� �� ������ null��ȯ
		
	}
	
	public int update(int bbsID, String bbsTitle, String bbsContent) { //write ��������
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?"; //Ư���� ���̵� �ش��ϴ� ����� ���� �ٲٰڴ�.
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
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?"; //���� �����ص� �ۿ����� ������ �����ְ� delete���ϰ� available�� 0����
		try 
		{
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return -1; //�����ͺ��̽�����
	}
	
}