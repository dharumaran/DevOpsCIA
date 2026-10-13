package com.university;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {

    @Test
    void shouldRegisterStudentForValidCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectInactiveCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                false,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
    }

    @Test
    void shouldRejectMissingPrerequisite() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS401"));
    }

    @Test
    void shouldAllowRegistrationWhenPrerequisiteIsCompleted() {

        Student student = new Student("S101", "Anita");
        student.addCompletedPrerequisite("CS301");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS401"));
    }

    @Test
    void shouldRejectDuplicateRegistration() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));

        boolean secondAttempt = service.register(student, course);

        assertFalse(secondAttempt);
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectRegistrationWhenCreditLimitIsExceeded() {

        Student student = new Student("S101", "Anita");

        // Simulate 20 existing credits
        student.addRegistration("CS101", 4);
        student.addRegistration("CS102", 4);
        student.addRegistration("CS201", 4);
        student.addRegistration("CS202", 4);
        student.addRegistration("CS203", 4);

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
    }
    // added tests
    @Test
void shouldRejectRegistrationWhenCourseIsFull() {

    RegistrationService service = new RegistrationService();

    Course course = new Course(
            "CS301", "Data Structures", 4, 1, true, null);

    Student firstStudent = new Student("S101", "Anita");
    Student secondStudent = new Student("S102", "Rahul");

    assertTrue(service.register(firstStudent, course));
    assertEquals(1, course.getCurrentEnrollment());

    int creditsBefore = secondStudent.getTotalCredits();

    boolean result = service.register(secondStudent, course);

    assertFalse(result);
    assertEquals(creditsBefore, secondStudent.getTotalCredits());
    assertEquals(1, course.getCurrentEnrollment());
    assertFalse(secondStudent.isRegistered("CS301"));
}

    
@Test
void shouldAllowRegistrationForLastAvailableSeat() {

    RegistrationService service = new RegistrationService();

    Course course = new Course(
            "CS301", "Data Structures", 4, 2, true, null);

    Student firstStudent = new Student("S101", "Anita");
    Student secondStudent = new Student("S102", "Rahul");

    assertTrue(service.register(firstStudent, course));
    assertEquals(1, course.getCurrentEnrollment());

    int creditsBefore = secondStudent.getTotalCredits();

    boolean result = service.register(secondStudent, course);

    assertTrue(result);
    assertEquals(2, course.getCurrentEnrollment());
    assertEquals(creditsBefore + 4, secondStudent.getTotalCredits());
    assertTrue(secondStudent.isRegistered("CS301"));
}
    @Test
void shouldRejectLowercaseDuplicateCourseCode() {

    RegistrationService service = new RegistrationService();
    Student student = new Student("S101", "Anita");

    Course originalCourse = new Course(
            "CS301", "Data Structures", 4, 30, true, null);

    Course duplicateCourse = new Course(
            "cs301", "Data Structures", 4, 30, true, null);

    assertTrue(service.register(student, originalCourse));

    int creditsBefore = student.getTotalCredits();

    boolean result = service.register(student, duplicateCourse);

    assertFalse(result);
    assertEquals(creditsBefore, student.getTotalCredits());
    assertEquals(1, originalCourse.getCurrentEnrollment());
    assertEquals(0, duplicateCourse.getCurrentEnrollment());
    assertEquals(1, student.getRegisteredCourses().size());
}

@Test
void shouldRejectMixedCaseDuplicateCourseCode() {

    RegistrationService service = new RegistrationService();
    Student student = new Student("S101", "Anita");

    Course originalCourse = new Course(
            "CS301", "Data Structures", 4, 30, true, null);

    Course duplicateCourse = new Course(
            "Cs301", "Data Structures", 4, 30, true, null);

    assertTrue(service.register(student, originalCourse));

    int creditsBefore = student.getTotalCredits();

    boolean result = service.register(student, duplicateCourse);

    assertFalse(result);
    assertEquals(creditsBefore, student.getTotalCredits());
    assertEquals(1, originalCourse.getCurrentEnrollment());
    assertEquals(0, duplicateCourse.getCurrentEnrollment());
    assertEquals(1, student.getRegisteredCourses().size());
}
@Test
void shouldAllowRegistrationForDifferentCourseCode() {

    RegistrationService service = new RegistrationService();
    Student student = new Student("S101", "Anita");

    Course firstCourse = new Course(
            "CS301", "Data Structures", 4, 30, true, null);

    Course secondCourse = new Course(
            "CS302", "Operating Systems", 3, 30, true, null);

    assertTrue(service.register(student, firstCourse));
    assertTrue(service.register(student, secondCourse));

    assertEquals(7, student.getTotalCredits());
    assertEquals(1, firstCourse.getCurrentEnrollment());
    assertEquals(1, secondCourse.getCurrentEnrollment());
    assertEquals(2, student.getRegisteredCourses().size());
}

}