package study_com.studytogetherproject.Moduls;

public class ItemNotification {
    private String name, nameOfTask, id, subjectOfUser, describtionOfUser, image;

    public ItemNotification(){}

    public ItemNotification(String name, String nameOfTask) {
        this.name = name;
        this.nameOfTask = nameOfTask;
    }

    public String getSubjectOfUser() {
        return subjectOfUser;
    }

    public void setSubjectOfUser(String subjectOfUser) {
        this.subjectOfUser = subjectOfUser;
    }

    public String getDescribtionOfUser() {
        return describtionOfUser;
    }

    public void setDescribtionOfUser(String describtionOfUser) {
        this.describtionOfUser = describtionOfUser;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameOfTask() {
        return nameOfTask;
    }

    public void setNameOfTask(String nameOfTask) {
        this.nameOfTask = nameOfTask;
    }
}
