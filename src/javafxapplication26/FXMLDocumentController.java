package javafxapplication26;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author LENOVO
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField txtFieldName;
    @FXML
    private TextField txtFieldMajor;
    @FXML
    private TextField txtFieldGrade;
    @FXML
    private TableView<Student> tableView;
    @FXML
    private TableColumn<Student, Integer> tcID;
    @FXML
    private TableColumn<Student, String> tcName;
    @FXML
    private TableColumn<Student, String> tcMajor;
    @FXML
    private TableColumn<Student, Double> tcGrade;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonUpdate;
    @FXML
    private Button buttonDelete;
    @FXML
    private TextField txtFieldSemester;

    @FXML
    private ComboBox<Integer> studentIdCombobox;

    @FXML
    private ComboBox<Integer> courseIdCombobox;

    @FXML
    private TableView<Registeration> tableView1;

    @FXML
    private TableColumn<Registeration, Integer> tcStudentId;

    @FXML
    private TableColumn<Registeration, Integer> tcCourseId;

    @FXML
    private TableColumn<Registeration, String> tcSemester;

    @FXML
    private Button buttonAddRegistration;

    private EntityManagerFactory emf;
    private Student student;
    private Course course;
    @FXML
    private Tab registerationTap;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emf = Persistence.createEntityManagerFactory("JavaFXApplication26PU");

        tcID.setCellValueFactory(new PropertyValueFactory("id"));
        tcName.setCellValueFactory(new PropertyValueFactory("name"));
        tcMajor.setCellValueFactory(new PropertyValueFactory("major"));
        tcGrade.setCellValueFactory(new PropertyValueFactory("grade"));

        show();
        tableView.getSelectionModel()
                .selectedItemProperty().addListener(e -> {
                    this.showSelectedStudent();
                });

    }

    private void showSelectedStudent() {

        student = tableView.getSelectionModel().getSelectedItem();
        if (student != null) {
            txtFieldName.setText(student.getName());
            txtFieldMajor.setText(student.getMajor());
            txtFieldGrade.setText(student.getGrade() + "");
        }

    }

    private void show() {
        EntityManager em = emf.createEntityManager();
        List<Student> students = em.createNamedQuery("Student.FindAll").getResultList();
        tableView.getItems().setAll(students);
        em.close();
    }

    @FXML
    private void buttonAddHandle(ActionEvent event) {
        String sName = txtFieldName.getText();
        String sMajor = txtFieldMajor.getText();
        Double sGrade = Double.parseDouble(txtFieldGrade.getText());

        Student student = new Student(sName, sMajor, sGrade);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
        em.close();
        clearFields();
        show();

    }

    @FXML
    private void buttonUpdateHandle(ActionEvent event) {
        EntityManager em = emf.createEntityManager();
        String sName = txtFieldName.getText();
        String sMajor = txtFieldMajor.getText();
        Double sGrade = Double.parseDouble(txtFieldGrade.getText());
        Student student = em.find(Student.class, this.student.getId());
        em.getTransaction().begin();
        student.setGrade(sGrade);
        student.setMajor(sMajor);
        student.setName(sName);
        em.getTransaction().commit();
        em.close();
        show();
        clearFields();

    }

    @FXML
    private void buttonDeleteHandle(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete " + this.student.getName() + "?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            EntityManager em = emf.createEntityManager();
            Student student = em.find(Student.class, this.student.getId());
            em.getTransaction().begin();
            em.remove(student);
            em.getTransaction().commit();
            em.close();
            show();
            clearFields();
        }

    }

    @FXML
    private void buttonAddRegistrationHandle(ActionEvent event) {
        EntityManager em = emf.createEntityManager();
        Student student = em.find(Student.class, this.studentIdCombobox.getValue());
        Course course = em.find(Course.class, this.courseIdCombobox.getValue());
        Registeration registeration = new Registeration(student, course,
                this.txtFieldSemester.getText());
        em.getTransaction().begin();
        em.persist(registeration);
        em.getTransaction().commit();
        em.close();
        clearFields();
        showRegisterations();

    }

    private void clearFields() {
        this.txtFieldGrade.setText("");
        this.txtFieldMajor.setText("");
        this.txtFieldName.setText("");
        this.txtFieldSemester.setText("");
        this.studentIdCombobox.setValue(null);
        this.courseIdCombobox.setValue(null);
    }

    @FXML
    private void registerationTapHandle(Event event) {
        tcStudentId.setCellValueFactory(new PropertyValueFactory("studentid"));
        tcCourseId.setCellValueFactory(new PropertyValueFactory("courseid"));
        tcSemester.setCellValueFactory(new PropertyValueFactory("semester"));
        showRegisterations();
        fillingCompoBoxes();

    }

    private void fillingCompoBoxes() {
        studentIdCombobox.getItems().clear();
        courseIdCombobox.getItems().clear();
        EntityManager em = emf.createEntityManager();
        List<Student> student = em.createNamedQuery("Student.FindAll")
                .getResultList();
        List<Course> courses = em.createNamedQuery("Course.FindAll")
                .getResultList();

        Iterator<Student> si = student.iterator();
        while (si.hasNext()) {
            studentIdCombobox.getItems().add(si.next().getId());
        }

        Iterator<Course> ci = courses.iterator();
        while (ci.hasNext()) {
            courseIdCombobox.getItems().add(ci.next().getId());
        }

    }

    public void showRegisterations() {
        EntityManager em = emf.createEntityManager();
        List<Registeration> registeration = em.createNamedQuery("Registeration.FindAll")
                .getResultList();
        tableView1.getItems().setAll(registeration);
        em.close();
    }

}
