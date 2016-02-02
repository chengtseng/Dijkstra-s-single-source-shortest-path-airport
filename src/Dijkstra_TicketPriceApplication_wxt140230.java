import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dijkstra_TicketPriceApplication_wxt140230
{	
	protected static ArrayList<Vertex> airportsList = new ArrayList<>();	
	protected static int connectionCount =0;
	
	public static void main( String [] args ) throws IOException
	{
		String yesNO ="Y";
		while(yesNO.equalsIgnoreCase("Y"))
		{
			 airportsList.clear();
			System.out.println("Please enter input file name of the txt file:");	
			
			Scanner input = new Scanner(System.in);			
			String inputFileName = input.nextLine();					
			
			try
			{
				createGlobalAirportsList(inputFileName);
				createAdjacencyList(inputFileName);
				
				System.out.println("Please enter the departure airport:");	
				String departureAirport = input.nextLine();
				
				
				Vertex theDVertex = findTheAirportVertexFromTheList(departureAirport);			
				
				/*dijkstraMethod*/
				DijkstraMethod(theDVertex);
				
				System.out.println("Please enter the arrival airport:");
				String arrivalAirport = input.nextLine();
				
				
				Vertex theAVertex = findTheAirportVertexFromTheList(arrivalAirport);			
				
				
				printPath( theAVertex);
				System.out.println(" ||  Cost is "+ theAVertex.dist+"  ||  connection:"+ String.valueOf(connectionCount-1) );
				System.out.println();				
			}
			catch(Exception ex)
			{
				System.out.println(ex.toString());
			}
			finally
			{
				System.out.println("Do you want to check another schedule? Enter (Y/N) :");	
				yesNO = input.nextLine();
			}
		}
		
		System.out.println("So sad, but thank you grader and professor, have a wonderful year ahead!");
	}

	
	public static void DijkstraMethod( Vertex start )
	{
		try
		{			
			for( Vertex v : airportsList)	
			{	
				v.known = false;			
			}
			
			start.dist = 0.0;	
			
			while( havingUnknownAirport() )						
			{				
				Vertex v = findMinDstAirport();			
				
				if( v == null )										
				{
					break;
				}
					
				v.known = true;				
					
				for( Edge e : v.adj )								
				{
					if ((v.dist + e.weight) < e.target.dist )
					{
						e.target.previous = v;
						e.target.dist = v.dist+e.weight;
					}
				}			
			}
		}
		catch(NullPointerException ex)
		{
			System.out.println("The airport you have entered is invalid!!");
			throw new NullPointerException("Invalid input. Airport not found!!");
		}
	}
		
	
	public static void createGlobalAirportsList(String inputFileName) throws IOException
	{
		BufferedReader bufReader = null;
		try
		{
			bufReader = new BufferedReader( new FileReader(inputFileName));
			
		}catch( IOException ex )
		{
			System.out.println("File not found exception handling");
			throw new IOException("File not found!!!!!!!!!!?");
		}		
		
		try
		{
			String str = bufReader.readLine();		
			while(str != null)
			{		
				String [] info = str.trim().split("\\s+");
				Vertex newAirportVertex = new Vertex(info[0]);
				airportsList.add(newAirportVertex);		
				str = bufReader.readLine();
			}
			
			bufReader.close();			
			
		}
		catch(IOException ex)
		{
			System.out.println("File break");
			throw new IOException("File break. File rebuild is needed");
		}
	}
	
	
	public static void createAdjacencyList(String inputFileName) throws IOException
	{
		BufferedReader bufReader2 = null;
		try
		{			
			bufReader2 = new BufferedReader( new FileReader(inputFileName));
		}
		catch( IOException ex )
		{
			System.out.println("File not found exception handling");
			throw new IOException("File not found");
		}
		
		try
		{
			String str2 = bufReader2.readLine();		
			int rowCount =1;		
			while(str2 != null)
			{			
				String [] info = str2.trim().split("\\s+");
				for(int i = 1; i < info.length; i+=2)
				{
					
					for(Vertex v : airportsList)
					{
						if(v.name.equals(info[i]))
						{
							airportsList.get(rowCount-1).adj.add(new Edge(v, Integer.valueOf(info[i+1])));
							
							break;
						}
					}				
				}
				rowCount++;
				str2 = bufReader2.readLine();
			}
			bufReader2.close();			
		}
		catch(IOException ex)
		{
			System.out.println("File break");
			throw new IOException("File break. File rebuild is needed");
		}
		catch(NumberFormatException ex)
		{
			System.out.println("File Format is wrong");
			throw new NumberFormatException("File format is wrong. File rebuild is needed");
		}
	}
	
	
	public static Vertex findTheAirportVertexFromTheList(String airportName)
	{		
		Vertex returnedVertex = null;			
		for(Vertex v : airportsList)
		{
			if(v.name.equalsIgnoreCase((airportName)))
			{
				returnedVertex = v;					
				break;
			}
		}
		return returnedVertex;		
	}
	
	
	public static boolean havingUnknownAirport()					
	{		
		for(Vertex v : airportsList)
		{
			if(v.known == false)
				
				return true;
		}		
		return false;		
	}
	
	
	public static Vertex findMinDstAirport()							
	{
		if( havingUnknownAirport() == false )	
			return null;
		
		Vertex minDstAirport = new Vertex("fake");				
		
		for(Vertex v : airportsList)
		{
			if(!(v.known) && (v.dist <= minDstAirport.dist ) )
			{
				minDstAirport = v;			
			}
		}
		return minDstAirport;
	}
	
	
	public static void printPath( Vertex v )
	{	
		connectionCount = 0;
		try
		{
			if( v.previous != null )
			{
				printPath( v.previous );
				connectionCount++;
				System.out.print( " -> " );
			}
			
		    System.out.print( v.name );
			
		}
		catch(NullPointerException ex)
		{
			System.out.println("Airport is not found");
			throw new NullPointerException("You have entered an invalid airport name.");
		}
	}
}


class Vertex implements Comparable<Vertex>
{
	public String name;														
	public ArrayList<Edge> adj = new ArrayList<>(); 						
	public boolean known;													
	public Double dist = Double.POSITIVE_INFINITY; 						
	public Vertex previous;													
	
	public Vertex( String name)
	{
		this.name = name;
	}
	
	@Override
	public int compareTo(Vertex v) 
	{
		if(this.dist <= v.dist )
			return -1;
		else
			return 1;
	}
}


class Edge implements Comparable<Edge>
{
	Vertex target;
	Double weight;
	
	Edge(Vertex target, int weight)
	{
		this.target = target;
		this.weight = (double)weight;
	}
	
	@Override
	public int compareTo(Edge e) 
	{
		if(this.weight <= e.weight )
			return -1;
		else
			return 1;
	}	
}