package Electronic_Table;
import javafx.util.Pair;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.Date;

public class Controller {

    public static class database implements Serializable {

        public ArrayList<database> tables;
        public String Date_of_creation;
        public String Last_modified_date;
        public String name;
        public ArrayList<String> Title_colums;
        public ArrayList<Pair<String,ArrayList<String>>>  table;
        public String owner;
        private int realY;
        private int xSize;
        private int ySize;
        private boolean Test;
        private int realX;


        public database(String name, int xSize, int ySize, String owner) {
            this.name = name;
            this.xSize = xSize;
            this.ySize = ySize;
            this.realX = xSize + 1;
            this.realY = ySize + 1;
            this.owner = owner;
            Last_modified_date = new Date().toString();
            Date_of_creation = Last_modified_date;

            Title_colums = new ArrayList<>();
            Title_colums.add("Student");
            for (int i = 1; i < realX; i++) {
                Title_colums.add("ColumnTitle");
            }

            table = new ArrayList<>();
            for (int i = 0; i < ySize; i++) {
                table.add(new Pair<>("name", new ArrayList<String>()));
                for (int j = 0; j < xSize; j++) {
                    table.get(i).getValue().add("0");
                }
            }
        }

        public database(ArrayList<String> Title_colums, boolean Test) {
            this.Title_colums = Title_colums;
            this.Test = Test;
        }

        public database() { tables = new ArrayList<>();}

        public database(boolean Test) {
            tables = new ArrayList<>();
            this.Test = Test;
        }

        public void change (String tableType, String Value, int x, int y)
        {
            if (tableType.equals("studName"))
            {
                table.set(y, new Pair<>(Value, table.get(y).getValue()));
            }
            if (tableType.equals("ColumnTitle"))
            {
                Title_colums.set(x,Value);
            }
            if (tableType.equals("valuesTable"))
            {
                table.get(y).getValue().set(x,Value);
            }
            Last_modified_date = new Date().toString();
            Main.backUp(Test);
        }

        public void addColumn()
        {
            xSize++;
            Title_colums.add("ColumnTitle");
            for (int i = 0; i < table.size(); i++)
            {
                table.get(i).getValue().add("0");
                Main.backUp(Test);
            }
            Last_modified_date = new Date().toString();
            backUp(Test);
        }

        private void backUp(boolean test) {
        }

        public void addString()
        {
            ySize++;
            realY++;
            ArrayList<String> tmp = new ArrayList<>();
            for (int j = 0; j < xSize; j++)
            {
                tmp.add("0");
            }
            table.add(new Pair<>("name", tmp));
            Last_modified_date = new Date().toString();
            backUp(Test);
        }

        public int returnIndex(String name) {
            for (int i = 0; i < tables.size(); i++)
            {
                if (tables.get(i).name.equals(name))
                    return i;
            }
            throw new RuntimeException("WrongName!");
        }

    public void createNewTable (String name, int xSize, int ySize, String owner)
    {

        tables.add(new database(name, xSize, ySize, owner));
        backUp(Test);
    }

    public int getSize ()
    {
        return tables.size();
    }

    public String getTableName (int index)
    {
        return tables.get(index).name;
    }



    public boolean containsTable (String name)
    {
        for (int i = 0; i < tables.size(); i++)
        {
            if (tables.get(i).name.equals(name))
                return true;
        }
        return false;
    }

    public void delete (String name)
    {
        if (containsTable(name))
            tables.remove(returnIndex(name));
        backUp(Test);
    }

    }
}
