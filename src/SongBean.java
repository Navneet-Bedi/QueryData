
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.sql.*;
//@Named(value = "transformBean")
//@Dependent
@ManagedBean
@SessionScoped
public class SongBean {
  private String data;
  
  public SongBean(){
  }
  public void setData(String s) {
	data=s;
    	data=data.toLowerCase();
  }
  public String getData() {
    return data;
	  
}

  public String processQuery() {
	String output="";
	String vsongs_query="";
	//if ((data.toLowerCase().contains(" vsongs"))
	//&& (data!=null) && (data.toLowerCase().contains("select")))
	if ((data!=null) && (data.toLowerCase().indexOf("select")!=-1)
	&& (data.toLowerCase().indexOf("vsongs")!=-1))	
        {
		try{
		Class.forName("oracle.jdbc.OracleDriver");
                    try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@134.154.10.161:1521:yyudb","hh2985","hh2985")) {
                        vsongs_query="(select vsongs.artist,vsongs.title,vsongs.year,vsongs.month,vsongs.day from yyu.songs, XMLTable('for $s in /songs/song return $s' passing object_value columns artist varchar2(40) path 'artist/text()',title varchar2(40) path 'title/text()',year varchar2(8) path 'date/year/text()',month varchar2(8) path 'date/month/text()',day varchar2(8) path 'date/day/text()')vsongs)";
                        data=data.replace("vsongs",vsongs_query);
                    try (PreparedStatement prep = conn.prepareStatement(data)) {
                        ResultSet res = prep.executeQuery();
                        ResultSetMetaData rsm = res.getMetaData();
                        
                        int cols = rsm.getColumnCount();
                        output+="<table border='1'><tr>";
                        
                        for (int i=1; i<=cols; i++){
                            int size ;
                            size = rsm.getColumnDisplaySize(i);
                            String name = rsm.getColumnName(i);
                            output+="<th>"+name+"</th>";
                        }
                        output+="</tr>";
                        while (res.next()) {
                            output+="<tr>";
                            for (int i = 1; i <= cols; i++) {
                                String s = res.getString(i);
                                output+="<td>"+s+"</td>";
                            }
                            output+="</tr>";
                        }
                        output+="</table><br/>";
                    }
 }

    } catch (Exception e)
    {
        //data=data.replace("(select vsongs.artist,vsongs.title,vsongs.year,vsongs.month,vsongs.day from yyu.songs, XMLTable('for $s in /songs/song return $s' passing object_value columns artist varchar2(40) path 'artist/text()',title varchar2(40) path 'title/text()',year varchar2(8) path 'date/year/text()',month varchar2(8) path 'date/month/text()',day varchar2(8) path 'date/day/text()')vsongs)","vsongs");
				output=e.toString()+" -- Incorrect Syntax <br/> -- Data entered is:  "+data+"<br/>";}
	}
	//else
	//{
    	//	output="Incorrect Syntax <br/> -- Data entered is: "+data+"<br/>";
//	}
data=data.replace("(select vsongs.artist,vsongs.title,vsongs.year,vsongs.month,vsongs.day from yyu.songs, XMLTable('for $s in /songs/song return $s' passing object_value columns artist varchar2(40) path 'artist/text()',title varchar2(40) path 'title/text()',year varchar2(8) path 'date/year/text()',month varchar2(8) path 'date/month/text()',day varchar2(8) path 'date/day/text()')vsongs)","vsongs");
    return output;
  }
}

			
