package Electronic_Table;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

@WebServlet ("/login")
public class Login extends HttpServlet {

    private class User {

        String userPassword;
        String kind ;

        public User(String userPassword, String kind ) {
            this.userPassword = userPassword;
            this.kind = kind ;
        }

    }

    private HashMap<String,User> userBase;

    @Override
    public void init() {
        userBase = new HashMap<>();

        File file = new File ("Base_of_user.txt");

        try {
            file.createNewFile();
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()){
                String [] tmp  = scanner.nextLine().split(":");
                if (tmp.length == 3)
                    if (tmp[0].length() != 0 && tmp[1].length() != 0)
                        if (tmp[2].equals("t")||tmp[2].equals("s"))
                            userBase.put(tmp[0], new User(tmp[1],tmp[2]));
            }

            System.out.println("Users: " + userBase.size());
            for (HashMap.Entry<String,User> entry: userBase.entrySet()){
                System.out.println(entry.getKey() + " kind: " + entry.getValue().kind );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getSession().getAttribute("USERNAME") == null) {
            PrintWriter out = resp.getWriter();
            req.getRequestDispatcher("header.html").include(req, resp);
            out.println("<div class=\"header\"><h1> Requiring User Login </h1></div>" +
                    "<style>\n" +
                    "        .header {\n" +
                    "            background: #FCF2E5;\n" +
                    "            padding: 50px 20px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "        .header h1 {\n" +
                    "            font-family: 'Merriweather', serif;\n" +
                    "            position: relative;\n" +
                    "            color: #FCF2E5;\n" +
                    "            background: #1b2631;\n" +
                    "            font-size: 2.5em;\n" +
                    "            font-weight: normal;\n" +
                    "            padding: 10px 40px;\n" +
                    "            display: inline-block;\n" +
                    "            margin: 0;\n" +
                    "            line-height: 1;\n" +
                    "        }\n" +
                    "       .header h1:before {\n" +
                    "            content: \"\";\n" +
                    "            position: absolute;\n" +
                    "            width: 100%;\n" +
                    "            height: 4px;\n" +
                    "            left: 0;\n" +
                    "            bottom: -15px;\n" +
                    "            background: #f69a73;\n" +
                    "        }\n" +
                    "        .header h1:after {\n" +
                    "            content: \"\";\n" +
                    "            position: absolute;\n" +
                    "            height: 0;\n" +
                    "            width: 80%;\n" +
                    "            border-top: 10px solid #f69a73;\n" +
                    "            border-left: 12px solid transparent;\n" +
                    "            border-right: 12px solid transparent;\n" +
                    "            left: 50%;\n" +
                    "            transform: translateX(-50%);\n" +
                    "            bottom: -25px;\n" +
                    "        }\n" +
                    "        @media (max-width: 500px) {\n" +
                    "            .three h1 {font-size: 1.8em;}\n" +
                    "        }\n" +
                    "        @media (max-width: 400px) {\n" +
                    "            .three h1 {\n" +
                    "                font-size: 1.5em;\n" +
                    "                padding: 10px 30px;\n" +
                    "            }\n" +
                    "        }\n" +
                    "    </style>");

            req.getRequestDispatcher("login.html").include(req, resp);
            out.println("</html></body>");
        }
        else {
            resp.sendRedirect("/table");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = req.getParameter("USERNAME");
        String password = req.getParameter("password");

        PrintWriter out = resp.getWriter();

        if( userBase.containsKey(userName) ){
            if (userBase.get(userName).userPassword.equals(password)) {
                HttpSession session = req.getSession();
                session.setAttribute("USERNAME", userName);
                session.setAttribute("kind ", userBase.get(userName).kind );
                resp.sendRedirect("/table");
            }
        }
        else{
            req.getRequestDispatcher("header.html").include(req, resp);
            out.print("<i><div class=\"header\"><h1> Error! No such user was found or the password was entered incorrectly. Try again.</h1></div></i>" +
                    "<style>\n" +
                    ".header {\n" +
                    "text-align: center;\n" +
                    "  text-align: center;\n" +
                    "  color: #f69a73;\n"+
                    "font-size: 1em;\n"+
                    "}</style>");

            req.getRequestDispatcher("login.html").include(req, resp);
            out.println("</html></body>");
        }

        out.close();

    }
}
