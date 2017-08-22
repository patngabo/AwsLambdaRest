package com.gcit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * Hello world!
 *
 */
public class App 
{
	public Connection getConnection() throws SQLException {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = (Connection) DriverManager.getConnection(
				"jdbc:mysql://firstrds.clorabjjqm6i.us-east-1.rds.amazonaws.com:3306/library", "rootroot", "rootroot");
		return conn;
	}
	
//	public void handler(InputStream input, OutputStream output, Context context) throws NumberFormatException, SQLException, ParseException, IOException {
//	    JSONParser jsonParser = new JSONParser();
//		JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(input, "UTF-8"));
//		String method = "POST";
//		
//		String sqlCommand = ""; String result = "";
//		String authorName = ""; Integer authorId = -1;
//		
//		if ("GET".equals(method)) {
//			System.out.println("GET Method");
//			authorId = Integer.parseInt((String) jsonObject.get("authorId"));
//			//authorId = (Integer) querystring.get("authorId");
//			sqlCommand = "SELECT * FROM `library`.`tbl_author` WHERE `authorId` = ?";
//			PreparedStatement prepareStatement1 = getConnection().prepareStatement(sqlCommand);
//			prepareStatement1.setInt(1, authorId);
//			ResultSet rs = prepareStatement1.executeQuery();
//			Author author = new Author();
//			while(rs.next()){
//				author.setAuthorId(rs.getInt("authorId"));
//				author.setAuthorName(rs.getString("authorName"));
//			}
//			result = author.toString();
//			System.out.println("RESULT: "+result);
//			output.write(result.getBytes(Charset.forName("UTF-8")));
//		}
//		else if ("POST".equals(method)){
//			System.out.println("POST Method");
//			authorName = (String) jsonObject.get("authorName");
//			sqlCommand = "INSERT INTO tbl_author (authorName) VALUES (?)";
//			//sqlCommand = "INSERT INTO `library`.`tbl_author` (`authorName`) OUTPUT Inserted.authorId, Inserted.authorName VALUES (?)";
//			PreparedStatement prepareStatement2 = getConnection().prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
//			prepareStatement2.setString(1, authorName);
//			prepareStatement2.executeUpdate(); 
//			ResultSet rs = prepareStatement2.getGeneratedKeys(); 
//			System.out.println("ResultSet: "+ rs.toString());
//			Author author = new Author();
//			while(rs.next()){
//				author.setAuthorId(rs.getInt(1));
//			}
//			author.setAuthorName(authorName);
//			result = author.toString();
//			output.write(result.getBytes(Charset.forName("UTF-8")));
//		}
//	}
	
	/* ______________________________________________________________ */
	
	public void handler(InputStream input, OutputStream output, Context context) throws NumberFormatException, SQLException, ParseException, IOException {
	    JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(input, "UTF-8"));
		
		String sqlCommand = ""; String result = "";
		String authorName = ""; Integer authorId = -1;
		JSONObject contex = (JSONObject) jsonObject.get("context");
		String method = (String) contex.get("http-method");
		JSONObject body = (JSONObject) jsonObject.get("body-json");
		JSONObject params = (JSONObject) jsonObject.get("params");
		JSONObject querystring = (JSONObject) params.get("querystring");
		
		if ("GET".equals(method)) {
			System.out.println("GET Method");
			authorId = Integer.parseInt((String) querystring.get("authorId"));
			sqlCommand = "SELECT * FROM `library`.`tbl_author` WHERE `authorId` = ?";
			PreparedStatement prepareStatement1 = getConnection().prepareStatement(sqlCommand);
			prepareStatement1.setInt(1, authorId);
			ResultSet rs = prepareStatement1.executeQuery();
			Author author = new Author();
			while(rs.next()){
				author.setAuthorId(rs.getInt("authorId"));
				author.setAuthorName(rs.getString("authorName"));
			}
			result = author.toString();
			System.out.println("RESULT: "+result);
			output.write(result.getBytes(Charset.forName("UTF-8")));
		}
		else if ("POST".equals(method)){
			System.out.println("POST Method");
			authorName = (String) body.get("authorName");
			sqlCommand = "INSERT INTO tbl_author (authorName) VALUES (?)";
			PreparedStatement prepareStatement2 = getConnection().prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
			prepareStatement2.setString(1, authorName);
			prepareStatement2.executeUpdate(); 
			ResultSet rs = prepareStatement2.getGeneratedKeys(); 
			System.out.println("ResultSet: "+ rs.toString());
			Author author = new Author();
			while(rs.next()){
				author.setAuthorId(rs.getInt(1));
			}
			author.setAuthorName(authorName);
			result = author.toString();
			output.write(result.getBytes(Charset.forName("UTF-8")));
		}
		else if ("DELETE".equals(method)) {
			System.out.println("DELETE Method");
			authorId = Integer.parseInt((String) querystring.get("authorId"));
			sqlCommand = "delete from tbl_author where authorId = ?";
			PreparedStatement prepareStatement1 = getConnection().prepareStatement(sqlCommand);
			prepareStatement1.setInt(1, authorId);
			prepareStatement1.executeUpdate(); 
//			ResultSet rs = prepareStatement1.executeQuery();
//			Author author = new Author();
//			while(rs.next()){
//				author.setAuthorId(rs.getInt("authorId"));
//				author.setAuthorName(rs.getString("authorName"));
//			}
//			result = author.toString();
//			System.out.println("RESULT: "+result);
			result = "Author deleted successfully!";
			output.write(result.getBytes(Charset.forName("UTF-8")));
		}
		else if ("PUT".equals(method)) {
			System.out.println("PUT Method");
			authorId = Integer.parseInt((String) querystring.get("authorId"));
			authorName = (String) body.get("authorName");
			sqlCommand = "update tbl_author set authorName =? where authorId = ?";
			PreparedStatement prepareStatement1 = getConnection().prepareStatement(sqlCommand);
			prepareStatement1.setInt(1, authorId);
			prepareStatement1.setString(2, authorName);
			ResultSet rs = prepareStatement1.executeQuery();
			Author author = new Author();
			while(rs.next()){
				author.setAuthorId(rs.getInt("authorId"));
				author.setAuthorName(rs.getString("authorName"));
			}
			result = author.toString();
			System.out.println("RESULT: "+result);
			output.write(result.getBytes(Charset.forName("UTF-8")));
		}
	}
	
	/* ______________________________________________________________ */

//	public String handler(InputStream input, Context context) throws NumberFormatException, SQLException, ParseException, IOException {
//	    JSONParser jsonParser = new JSONParser();
//		JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(input, "UTF-8"));
//		
//		//System.out.println(input.toString());
//		//System.out.println(jsonObject.toJSONString());
//		
//		String sqlCommand = ""; String result = "";
//		String method = "GET";
//		String authorName = "";
//		Integer authorId = -1;
//		//String method = (String) jsonObject.get("http-method");
//		//JSONObject body = (JSONObject) jsonObject.get("body-json");
//		
//		if ("GET".equals(method)) {
//			System.out.println("GET Method");
//			authorId = Integer.parseInt((String) jsonObject.get("authorId"));
//			System.out.println(authorId);
//			sqlCommand = "SELECT * FROM `library`.`tbl_author` WHERE `authorId` = ?";
//			PreparedStatement prepareStatement1 = getConnection().prepareStatement(sqlCommand);
//			prepareStatement1.setInt(1, authorId);
//			ResultSet rs = prepareStatement1.executeQuery();
//			System.out.println(rs.toString());
//			Author author = new Author();
//			while(rs.next()){
//				author.setAuthorId(rs.getInt("authorId"));
//				author.setAuthorName(rs.getString("authorName"));
//			}
//			result = author.toString();
//			System.out.println(result);
//			return result;
//			//return (JSONObject)jsonParser.parse(result);
//			//return new ResponseClass(author);
//		}
//		else if ("POST".equals(method)){
//			System.out.println("POST Method");
//			authorName = (String) jsonObject.get("authorName");
//			sqlCommand = "INSERT INTO `library`.`tbl_author` (`authorName`) VALUES (?)";
//			PreparedStatement prepareStatement2 = getConnection().prepareStatement(sqlCommand);
//			prepareStatement2.setString(1, authorName);
//			prepareStatement2.executeUpdate();
//		}
//		
//		return "okay";
//		//return (JSONObject)jsonParser.parse("null");
//		//return new ResponseClass(authorName, authorId);
//	}
	
	
    
}
