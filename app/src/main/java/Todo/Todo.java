package Todo;

public class Todo {
    private String title, descr;
    //higher priority = lower number
    private int priority, id;
    private final int defaultPriority = 5;

    public Todo(){
        title = "";
        descr = "";
        priority = defaultPriority;
    }

    public Todo(int todoID, String todoTitle, String todoDescr){
        title = todoTitle;
        descr = todoDescr;
        priority = defaultPriority;
        id = todoID;
    }

    public Todo(int todoID, String todoTitle, String todoDescr, int todoPriority){
        title = todoTitle;
        descr = todoDescr;
        priority = todoPriority;
        id = todoID;
    }

    public Todo(String todoTitle, String todoDescr, int todoPriority){
        title = todoTitle;
        descr = todoDescr;
        priority = todoPriority;
    }

    /*
    Setter methods
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setID(int id) {
        this.id = id;
    }

    /*
        Getter methods
         */
    public String getTitle() {
        return title;
    }

    public String getDescr() {
        return descr;
    }

    public int getPriority() {
        return priority;
    }

    public int getID() {
        return id;
    }
}
