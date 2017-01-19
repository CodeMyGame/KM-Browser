
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Register extends HttpServlet{
    
     static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/kmbrowser";
   static final String USER = "root";
   static final String PASS = "admin";
   

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text");
        try{
        String pass = req.getParameter("pass");
        String email = req.getParameter("email");
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
            PreparedStatement ps = con.prepareStatement("insert into users values(?,?)");
            ps.setString(1,email);
            ps.setString(2,pass);
            int i =ps.executeUpdate();
            if(i!=0){
              resp.getWriter().write("success");
            }
           
        
    }catch(Exception e){
        
    }
}
}