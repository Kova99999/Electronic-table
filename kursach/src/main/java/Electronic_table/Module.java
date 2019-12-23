package Electronic_Table;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class Module {
    public static class MyTable implements Serializable {
        private int realX;
        private int realY;
        private int xSize;
        private int ySize;
        private  String name;
        private String owner;
        private String Date_of_creation;
        private String Last_modified_date;
        private ArrayList<String> Title_colums;
        private ArrayList<Pair<String, ArrayList<String>>> table;

        public MyTable(String name, int xSize, int ySize, String owner) {
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

    }
}
