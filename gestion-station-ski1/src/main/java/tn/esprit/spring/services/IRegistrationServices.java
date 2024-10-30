package tn.esprit.spring.services;

import tn.esprit.spring.dto.RegistrationDTO;
import tn.esprit.spring.entities.Support;

import java.util.List;

public interface IRegistrationServices {

	RegistrationDTO addRegistrationAndAssignToSkier(RegistrationDTO registrationDTO, Long numSkier);

	RegistrationDTO assignRegistrationToCourse(Long numRegistration, Long numCourse);

	RegistrationDTO addRegistrationAndAssignToSkierAndCourse(RegistrationDTO registrationDTO, Long numSkieur, Long numCours);

	List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support);
}