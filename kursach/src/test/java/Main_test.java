import Electronic_Table.Controller;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Main_test {

    private Controller.database tableBase;

    @Before
    public void init(){
        tableBase = new Controller.database(true);
    }

    @Test
    public void Test_Size(){
        assertEquals(0, tableBase.getSize());
    }


    @Test
    public void Test_Index (){
        tableBase.createNewTable("abc", 2, 2, "teacher");
        assertEquals(0, tableBase.returnIndex("abc"));
        tableBase.delete("abc");
    }

    @Test
    public void Test_Name(){
        tableBase.createNewTable("abc", 2, 2, "teacher");
        assertEquals("abc", tableBase.getTableName(0));
        tableBase.delete("abc");
    }

    @Test
    public void Test_contains() {
        tableBase.createNewTable("a11", 2, 2, "teacher");
        assertTrue(tableBase.containsTable("a11"));
        tableBase.delete("a11");
    }

}
