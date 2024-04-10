package hotelManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;





public class HotelReservationSystem {

	private static final String url ="jdbc:mysql://localhost:3306/hotel_db";
	private static final String username ="root";
	private static final String password ="root";
	
	public static void main(String[] args) throws ClassNotFoundException ,SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		System.out.println(e.getMessage());
		}
		
		try {
			Connection con=DriverManager.getConnection(url,username,password);
			while(true) {
				System.out.println();
				System.out.println("*-*-*-HOTEL MANAGEMENT SYSTEM-*-*-*");
				Scanner sc=new Scanner(System.in);
				System.out.println("1.Researve a room");
				System.out.println("2.View Reservations");
				System.out.println("3.Get Room Number");
				System.out.println("4.Update Reservations");
				System.out.println("5.Delete reservations");
				System.out.println("0.Exit");
				System.out.println("Choose an Option : ");
				int choice=sc.nextInt();
				
				switch(choice) {
				case 1 : 
					reserveRoom(con,sc);
				    break;
				         
				case 2: 
					viewReservations(con);
				    break;
						
				case 3 : 
					getRoomNumber(con,sc);
					break;
						
				case 4: 
					updateReservations(con,sc);
					break;
						
				case 5: 
					deleteReservations(con,sc);
					break;
						
				case 0:
					exit();
					sc.close();
					return;
					
				default:
					System.out.println("Invalid Choice. Try Again Please..!");
				
				}
			}
		}
			catch(SQLException e){
			System.out.println(e.getMessage());
				
			}
		catch(InterruptedException e) {
			throw new RuntimeException(e);
			
		}
		
		
	}
	
	///Reservations of Room
	private static void reserveRoom(Connection con,Scanner sc) {
		try {
			System.out.print("Enter Guest Name : ");
			String guestName=sc.next();
			sc.nextLine();
			
			System.out.print("Enter Room Number : ");
			int roomNumber=sc.nextInt();
			
			System.out.print("Enter Contact Number :  ");
			String contactNumber=sc.next();
			
			String query="Insert into reservations (guest_name,roomNo,ContactNumber) "+
			"values ('"+guestName+"',"+roomNumber+",'"+contactNumber+"')";
			
			try (Statement stm=con.createStatement()){
				int affRows=stm.executeUpdate(query);
				
				if(affRows>0) {
					System.out.println("Reservation SuccessFul..!");
				}
				else {
					System.out.println("Reservation Failed ..!");
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	///Details Of Reservations
	
	private static void viewReservations(Connection con) throws SQLException {
		String query="Select res_id,guest_name,roomNo,ContactNumber,Res_Date from reservations ";
		try (Statement stm=con.createStatement();
				ResultSet rs=stm.executeQuery(query))
		{
			System.out.println("Current reservation :");
			System.out.println("+--------------*---------*---------------*------------------*---------------------------------------*");
			System.out.printf("| %-15s | %-15s | %-13s | %-18s | %-23s |\n", "Reservation ID", "Guest", "Room Number", "Contact Number", "Reservation Date");
			System.out.println("+--------------*---------*---------------*------------------*---------------------------------------*");
			
			while(rs.next()) {
				int resId=rs.getInt("res_id");
				String guestName=rs.getString("guest_name");
				int roomNo=rs.getInt("roomNo");
				String contactNumber=rs.getString("ContactNumber");
				String ResDate=rs.getString("Res_Date").toString();
				
				System.out.printf("| %-15d | %-15s | %-13d | %-18s | %-23s | \n",
						
						resId,guestName,roomNo,contactNumber,ResDate);
			}
			
			System.out.println("+--------------*---------*---------------*------------------*---------------------------------------*");
		}
	}
	
	///Getting Room Number 
	
	private static void getRoomNumber(Connection con,Scanner sc) {
		try {
			System.out.print("Enter reservation ID : ");
			int resId=sc.nextInt();
			System.out.print("Enter guest Name : ");
			String guestName=sc.next();
			
			String query="select roomNo from reservations "+
			" where res_id = "+resId+" AND guest_Name = '"+guestName+"'";
			
			try(Statement stm=con.createStatement();
				ResultSet rs= stm.executeQuery(query)){
				
				if(rs.next()) {
					int roomNo=rs.getInt("roomNo");
					System.out.println(" Room number for Reservation Id  "+resId+" and Guest "+guestName+ " is : "+roomNo);
				}
				else {
					System.out.println("Reservation not found for the given ID and guest Name. ");
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	///Update the details of reservations
	
	private static void updateReservations(Connection con,Scanner sc) {
		try {
			System.out.println("Enter Reservation ID to update : ");
			int resId=sc.nextInt();
			sc.nextLine();
			
			if(!reservationExists(con,resId)) {
				System.out.println("The Reservations not found for the given ID..!");
				return;
			}
			
			System.out.println("Enter new Guest Name : ");
			String newGuestName=sc.nextLine();
			System.out.println("Enter New Room Number : ");
			int newRoomNumber=sc.nextInt();
			System.out.println("Enter New Contact Number : ");
			String newContactNumber=sc.next();
			
			String query="update reservations set guest_name = '"+newGuestName+"',"+
					"roomNo = "+newRoomNumber +","+
					"ContactNumber = "+newContactNumber +"'"+
					"where res_id = "+resId;
			
			try(Statement stm=con.createStatement()){
				int affRows=stm.executeUpdate(query);
				
				if(affRows>0) {
					System.out.println("Reservation updated successfully..!");
				}
				else {
					System.out.println("Reservation Update failed..!");
				}
				
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//deleting the reservations
	
	private static void deleteReservations(Connection con,Scanner sc) {
		try {
			System.out.println("Enter Reservation ID to Delete : ");
			int resId=sc.nextInt();
			
			if(!reservationExists(con,resId)) {
				System.out.println("Reservations not found for the Given ID .!!");
				return;
			}
			
			String query="delete from reservations where res_id = "+resId;
			
			try(Statement stm=con.createStatement()){
				int affRows=stm.executeUpdate(query);
				
				if(affRows >0) {
					System.out.println("Reservation Deleted Successfully ...!");
				}
				else {
					System.out.println("Reservation Deletion failed...!");
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//check if the reservation exists or not
	
	private static boolean reservationExists(Connection con,int resId) {
		try {
			String query="select res_id from reservations where res_id = "+resId;
			
			try(Statement stm=con.createStatement();
					ResultSet rs=stm.executeQuery(query)){
				return rs.next();
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//exit method
	
	public static void exit() throws InterruptedException {
		System.out.print("Exiting System");
		int i=5;
		while(i!=0) {
			System.out.print(".");
			Thread.sleep(450);
			i--;
		}
		System.out.println();
		System.out.println("---------------Thank You For Using Hotel management System-------------");
	}
	
}
