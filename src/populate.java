
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class populate {
	
	Connection mainConnection = null;
	
	public populate()
	{
		ConnectToDB();
	}
	
	void populateStudentsTable(String filename)
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line;
			PreparedStatement query = null;
			String insertString = null;
			System.out.println("Inserting values into STUDENTS...");
			while((line = in.readLine()) != null)
			{
				String[] lineValues = line.split(",");
				String personID = lineValues[0].trim();
				int x = Integer.parseInt(lineValues[1].trim());
				int y = Integer.parseInt(lineValues[2].trim());
				
				//code for insertion into table
				insertString = "INSERT INTO STUDENTS VALUES(?, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(?,?,NULL), NULL, NULL))";
				query = mainConnection.prepareStatement(insertString);
				query.setString(1, personID);
				query.setInt(2, x);
				query.setInt(3, y);
				query.executeUpdate();
				mainConnection.commit();
			}
			//System.out.println(insertString);
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	void populateBuildingsTable(String filename)
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line;
			Statement query = null;
			String insertString = null;
			System.out.println("Inserting values into BUILDINGS...");
			while((line = in.readLine()) != null)
			{
				String[] lineValues = line.split(",");
				String buildingID= lineValues[0].trim();
				String buildingName = lineValues[1].trim();
				int verticesCount = Integer.parseInt(lineValues[2].trim());
				int[] verticesValues = new int[2*verticesCount];
				for(int i = 0; i < 2*verticesCount ; i++ )
				{
					verticesValues[i] = Integer.parseInt(lineValues[i + 3].trim());
				}
				String x = null;
				for(int i = 0; i < verticesCount; i++)
				{
					if(i != verticesCount - 1)
						x += "?,?,";
					else
						x += "?,?";
				}
				//insertString = "INSERT INTO BUILDINGS VALUES(?, ?, ?, SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1,1003,1), SDO_ORDINATE_ARRAY(" + x + ")))";
				insertString = "INSERT INTO BUILDINGS VALUES('" + buildingID + "', '" + buildingName + "', " + verticesCount + ", SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1,1003,1), SDO_ORDINATE_ARRAY(";
				query = mainConnection.createStatement();
				//query = mainConnection.prepareStatement(insertString);
				//query.setString(1, buildingID);
				//query.setString(2, buildingName);
				//query.setInt(3, verticesCount);
				for(int i = 0; i < (verticesValues.length); i++)
				{
					//query.setInt(i + 4, verticesValues[i]);
					if(i != verticesValues.length - 1)
						insertString += verticesValues[i] + ",";
					else
						insertString += verticesValues[i];
				}
				insertString += "," + verticesValues[0] + "," + verticesValues[1];
				insertString += ")))";
				query.executeQuery(insertString);
				mainConnection.commit();
			}
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	void populateAnnouncementSystemsTable(String filename)
	{
		
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line;
			PreparedStatement query = null;
			String insertString = null;
			System.out.println("Inserting values into ANNOUNCEMENT SYSTEMS...");
			while((line = in.readLine()) != null)
			{
				String[] lineValues = line.split(",");
				String asID = lineValues[0].trim();
				int x = Integer.parseInt(lineValues[1].trim());
				int y = Integer.parseInt(lineValues[2].trim());
				int radius = Integer.parseInt(lineValues[3].trim());
				
				//code for insertion into table
				insertString = "INSERT INTO ANNOUNCEMENTSYSTEMS VALUES(?, ?, SDO_GEOMETRY(2001, NULL, SDO_POINT_TYPE(?,?,NULL), NULL, NULL), SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1,1003,4), SDO_ORDINATE_ARRAY(?,?,?,?,?,?)))";
				query = mainConnection.prepareStatement(insertString);
				query.setString(1, asID);
				query.setInt(2, radius);
				query.setInt(3, x);
				query.setInt(4, y);
				query.setInt(5, x);
				query.setInt(6, y + radius);
				query.setInt(7, x);
				query.setInt(8, y - radius);
				query.setInt(9, x + radius);
				query.setInt(10, y);
				query.executeUpdate();
				mainConnection.commit();
			}
		}
		
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	//First delete data present in all tables
	void deleteCurrentData(String[] filenames)
	{
		try 
		{
			System.out.println("Deleting data from Buildings...");
			String deleteQuery = "delete from Buildings";
			PreparedStatement deleteTable = mainConnection.prepareStatement(deleteQuery);
			deleteTable.executeUpdate();
			System.out.println("Deleting data from Students...");
			deleteQuery = "delete from Students";
			deleteTable = mainConnection.prepareStatement(deleteQuery);
			deleteTable.executeUpdate();
			System.out.println("Deleting data from AnnouncementSystems...");
			deleteQuery = "delete from AnnouncementSystems";
			deleteTable = mainConnection.prepareStatement(deleteQuery);
			deleteTable.executeUpdate();
			mainConnection.commit();
		} 
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void ConnectToDB()
 	{
		try
		{
			// loading Oracle Driver
    		//System.out.print("Looking for Oracle's jdbc-odbc driver ... ");
	    	DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
	    	//System.out.println(", Loaded.");

			String URL = "jdbc:oracle:thin:@localhost:1521:hw2";
	    	String userName = "system";
	    	String password = "hw2";

	    	//System.out.print("Connecting to DB...");
	    	mainConnection = DriverManager.getConnection(URL, userName, password);
	    	//System.out.println(", Connected!");

    		//mainStatement = mainConnection.createStatement();

   		}
   		catch (Exception e)
   		{
     		System.out.println( "Error while connecting to DB: "+ e.toString() );
     		System.out.println(e.getMessage());
     		System.exit(-1);
   		}
 	}

	
	public static void main(String[] args) {
		
		populate p = new populate();
		int numberOfFiles = args.length;
		if(numberOfFiles < 3)
			System.out.println("Please enter 3 file names!");
		else
		{
			p.deleteCurrentData(args);
			p.populateBuildingsTable(args[0]);
			p.populateStudentsTable(args[1]);
			p.populateAnnouncementSystemsTable(args[2]);
		}
		
	}

}
