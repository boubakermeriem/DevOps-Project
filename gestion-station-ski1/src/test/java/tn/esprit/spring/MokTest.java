package tn.esprit.spring;

import tn.esprit.spring.dto.RegistrationDTO;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.RegistrationServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MokTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRegistrationAndAssignToSkier() {
        RegistrationDTO registrationDTO = createSampleRegistrationDTO("1");
        Skier skier = new Skier();
        skier.setNumSkier(1L);

        // Mock de la récupération du skieur et de l'enregistrement de la registration
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenAnswer(invocation -> {
            Registration registration = invocation.getArgument(0);
            registration.setNumWeek(1);
            registration.setSkier(skier); // Affecter le skieur à l'enregistrement
            return registration;
        });

        RegistrationDTO result = registrationServices.addRegistrationAndAssignToSkier(registrationDTO, 1L);

        assertNotNull(result, "Le résultat ne doit pas être nul.");
        assertEquals(1L, result.getSkierId(), "L'ID du Skier doit correspondre.");
    }



    @Test
    void testAssignRegistrationToCourse() {
        Registration registration = new Registration();
        registration.setNumWeek(1);
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        registration.setSkier(skier); // Assurez-vous que le skieur est bien défini dans la registration

        Course course = new Course();
        course.setNumCourse(1L);

        // Mock de la récupération des entités et de l'enregistrement de la registration
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenAnswer(invocation -> {
            Registration reg = invocation.getArgument(0);
            reg.setCourse(course); // Assigner le cours à l'enregistrement
            return reg;
        });

        RegistrationDTO result = registrationServices.assignRegistrationToCourse(1L, 1L);

        assertNotNull(result, "Le résultat ne doit pas être nul.");
        assertEquals(1L, result.getCourseId(), "L'ID du Course doit correspondre.");
    }



    @Test
    void testAddRegistrationAndAssignToSkierAndCourse() {
        RegistrationDTO registrationDTO = createSampleRegistrationDTO("1");
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        Course course = new Course();
        course.setNumCourse(1L);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong()))
                .thenReturn(0L); // Remplacez par 0 si le type de retour est int
        when(registrationRepository.save(any(Registration.class))).thenAnswer(invocation -> {
            Registration registration = invocation.getArgument(0);
            registration.setNumWeek(1);
            return registration;
        });

        RegistrationDTO result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registrationDTO, 1L, 1L);

        assertNotNull(result, "Le résultat ne doit pas être nul.");
        assertEquals(1L, result.getSkierId(), "L'ID du Skier doit correspondre.");
        assertEquals(1L, result.getCourseId(), "L'ID du Course doit correspondre.");
    }

    @Test
    void testNumWeeksCourseOfInstructorBySupport() {
        Long numInstructor = 1L;
        Support support = Support.SKI;
        List<Integer> expectedWeeks = Arrays.asList(1, 2, 3);

        when(registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support)).thenReturn(expectedWeeks);

        List<Integer> result = registrationServices.numWeeksCourseOfInstructorBySupport(numInstructor, support);

        assertNotNull(result, "La liste des semaines ne doit pas être nulle.");
        assertEquals(expectedWeeks.size(), result.size(), "La taille de la liste des semaines doit correspondre.");
        assertEquals(expectedWeeks, result, "La liste des semaines doit correspondre aux semaines attendues.");
    }

    // Méthodes utilitaires pour créer des objets d'exemple
    private RegistrationDTO createSampleRegistrationDTO(String otherField) {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setOtherField(otherField);
        return registrationDTO;
    }

}