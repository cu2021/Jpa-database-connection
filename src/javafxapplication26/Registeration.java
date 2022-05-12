package javafxapplication26;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity

    @NamedQuery(name = "Registeration.FindAll",
            query = "Select r From Registeration r")

public class Registeration implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "studentid")
    private Student student;
    @Id
    @ManyToOne
    @JoinColumn(name = "courseid")
    private Course course;
    @Id
    private String semester;

    public Registeration() {
    }

    public Registeration(Student studentId, Course courseId, String semester) {
        this.student = studentId;
        this.course = courseId;
        this.semester = semester;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getStudentid() {
        return student.getId();
    }

    public Integer getCourseid() {
        return course.getId();
    }

}
