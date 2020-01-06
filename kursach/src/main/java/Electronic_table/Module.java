package Electronic_Table;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.*;
import java.util.ArrayList;


@WebServlet({"/table", "/New_Table", "/add", "/show", "/change", "/delete", "/Add_String", "/Add_Column"})

public class Module extends HttpServlet {

    private static Controller.database BASE;

    protected static synchronized void backUp(boolean test)
    {
        if (!test) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("backUp.txt"));
                out.writeObject(BASE.tables);
                System.out.println("BackUp");
            } catch (IOException e) {
                System.out.println("BackUp:" + e.getMessage());
            }
        }
    }

    @Override
    public void init() {
        ArrayList<String> title_colums = null;
        BASE = new Controller.database(title_colums, false);
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("backUp.txt"));
            ArrayList<Controller.database> tmp = (ArrayList<Controller.database>) in.readObject();
            if (!tmp.isEmpty())
                BASE.tables = tmp;
            System.out.println("Success");
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println("Error:" + e.getMessage());
        }
    }

}

