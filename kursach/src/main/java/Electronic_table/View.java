package Electronic_Table;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class View extends HttpServlet {
    private static final Controller.database BASE = new Controller.database();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession httpSession = req.getSession();
        PrintWriter out = resp.getWriter();
        if (httpSession.getAttribute("USERNAME") == null){
            resp.sendRedirect("/login");
            return;
        }
        req.getRequestDispatcher("header.html").include(req,resp);
        out.println(  "You are logged in as <i>" + httpSession.getAttribute("USERNAME") + "</i><br>");
        if (((HttpSession) httpSession).getAttribute("kind ").equals("t"))
        {
            out.println("| <a href=\"New_Table\">Add new grade journal</a> | ");
        }
        req.getRequestDispatcher("links.html").include(req,resp);
        if (req.getRequestURI().contains("New_Table") && req.getSession().getAttribute("kind ").equals("t"))
        {
            req.getRequestDispatcher("new_table.html").include(req,resp);
        }
        if (req.getRequestURI().contains("show"))
        {
            int index = BASE.returnIndex(req.getParameter("id"));
            out.println("<p>The name of table: <b>" + BASE.tables.get(index).name + "</b></p>");
            out.println("<p>Date of creation: " + BASE.tables.get(index).Date_of_creation+ "</p>");
            out.println("<p>Last modified date: " + BASE.tables.get(index).Last_modified_date + "</p>");
            out.println(Show_Table(index,req));
            out.println("</body></html>");
            return;

        }
        if (req.getRequestURI().contains("change") && req.getSession().getAttribute("kind").equals("t"))
        {
            req.getRequestDispatcher("change.html").include(req,resp);
            out.println("<input type=\"hidden\" name=\"TITLE\" value=\""+ req.getParameter("TITLE") +"\">");
            out.println("<input type=\"hidden\" name=\"tableType\" value=\""+ req.getParameter("tableType") +"\">");
            out.println("<input type=\"hidden\" name=\"x\" value=\""+ req.getParameter("x") +"\">");
            out.println("<input type=\"hidden\" name=\"y\" value=\""+ req.getParameter("y") +"\">");
            out.println("</form></body></html>");
            return;
        }
        if (req.getRequestURI().contains("Add_String"))
        {
            int index = BASE.returnIndex(req.getParameter("TITLE"));
            if (httpSession.getAttribute("USERNAME").equals(BASE.tables.get(index).owner))
                BASE.tables.get(index).addString();
            resp.sendRedirect("show?id=" + req.getParameter("TITLE"));
        }
        if (req.getRequestURI().contains("Add_Column"))
        {
            int index = BASE.returnIndex(req.getParameter("TITLE"));
            if (httpSession.getAttribute("USERNAME").equals(BASE.tables.get(index).owner))
                BASE.tables.get(index).addColumn();
            resp.sendRedirect("show?id=" + req.getParameter("TITLE"));
        }
        if (req.getRequestURI().contains("delete"))
        {
            int index = BASE.returnIndex(req.getParameter("TITLE"));
            if (httpSession.getAttribute("USERNAME").equals(BASE.tables.get(index).owner))
            {
                BASE.delete(req.getParameter("TITLE"));
            }
        }
        for (int i = 0; i < BASE.getSize(); i++)
        {
            out.println("<a href=\"show?id=" + BASE.getTableName(i) + "\">" + BASE.getTableName(i)
                    + "</a> ");
            if (httpSession.getAttribute("USERNAME").equals(BASE.tables.get(i).owner))
                out.println("[<a href=\"delete?TITLE=" + BASE.getTableName(i) +"\">Delete</a>]");
            out.println("</br>");
        }
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        if((req.getSession().getAttribute("USERNAME") != null) &&
                (req.getSession().getAttribute("kind").equals("t")))
        {
            if (req.getRequestURI().contains("change"))
            {
                if (BASE.containsTable(req.getParameter("TITLE")))
                {
                    int index = BASE.returnIndex(req.getParameter("TITLE"));
                    if (req.getSession().getAttribute("USERNAME").equals(BASE.tables.get(index).owner))
                    {
                        BASE.tables.get(BASE.returnIndex(req.getParameter("TITLE"))).change(req.getParameter("tableType"), req.getParameter("value"), Integer.parseInt(req.getParameter("x")), Integer.parseInt(req.getParameter("y")));
                    }
                }
                resp.sendRedirect("show?id=" + req.getParameter("TITLE"));
                return;
            }
        }

        if((req.getSession().getAttribute("USERNAME") != null) &&
                req.getSession().getAttribute("kind").equals("t"))
        {
            if (req.getRequestURI().contains("add"))
            {
                if (!BASE.containsTable(req.getParameter("TITLE")))
                {
                    BASE.createNewTable(req.getParameter("TITLE"), Integer.parseInt(req.getParameter("xSize")), Integer.parseInt(req.getParameter("ySize")), req.getSession().getAttribute("USERNAME").toString());
                }
                resp.sendRedirect("/table");
                return;
            }
        }
    }

    private String Show_Table (int index, HttpServletRequest req)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String userType = req.getSession().getAttribute("kind").toString();
        boolean OWNER = userType.equals("t") && req.getSession().getAttribute("USERNAME").equals(BASE.tables.get(index).owner);
        stringBuilder.append("<table border=\"1\" cellpadding=\"7\" width=\"100%\"><tr>");

        for (int i = 0; i < BASE.tables.get(index).Title_colums.size(); i++)
        {
            stringBuilder.append("<td align=\"center\">");
            if (OWNER)
            {
                stringBuilder.append("<a href =\"change?tableType=TitleColumn&y=0&x=").append(i).append("&TITLE=").append(BASE.tables.get(index).name).append("\">");
            }
            stringBuilder.append(BASE.tables.get(index).Title_colums.get(i)).append("</a></td>");
        }
        stringBuilder.append("</tr>");

        for (int i = 0; i < BASE.tables.get(index).table.size(); i++)
        {
            stringBuilder.append("<td align=\"center\">");
            if (OWNER)
            {
                stringBuilder.append("<a href =\"change?tableType=studName&y=").append(i).append("&x=0&TITLE=").append(BASE.tables.get(index).name).append("\">");
            }
            stringBuilder.append(BASE.tables.get(index).table.get(i).getKey()).append("</a></td>");

            for (int j = 0; j < BASE.tables.get(index).table.get(i).getValue().size(); j++) {

                stringBuilder.append("<td  align=\"center\">");
                if (OWNER)
                {
                    stringBuilder.append("<a href =\"change?tableType=valuesTable&y=").append(i).append("&x=").append(j).append("&TITLE=").append(BASE.tables.get(index).name).append("\">");
                }
                stringBuilder.append(BASE.tables.get(index).table.get(i).getValue().get(j)).append("</a></td>");

            }
            stringBuilder.append("</tr>");
        }
        stringBuilder.append("</table>");

        if (OWNER)
        {
            stringBuilder.append("[<b><a href=\"Add_String?tableName=" + BASE.tables.get(index).name + "\">Add string</a></b>]");
            stringBuilder.append("[<b><a href=\"Add_Column?tableName=" + BASE.tables.get(index).name + "\">Add column</a></b>]");

        }
        return stringBuilder.toString();

    }
}
