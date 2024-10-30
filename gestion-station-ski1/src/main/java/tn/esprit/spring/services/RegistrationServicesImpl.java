package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dto.RegistrationDTO;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class RegistrationServicesImpl implements IRegistrationServices {

    private final IRegistrationRepository registrationRepository;
    private final ISkierRepository skierRepository;
    private final ICourseRepository courseRepository;

    @Override
    public RegistrationDTO addRegistrationAndAssignToSkier(RegistrationDTO registrationDTO, Long numSkier) {
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        if (skier == null) {
            return null; // Ou gérer l'erreur selon votre logique
        }

        Registration registration = convertToEntity(registrationDTO);
        registration.setSkier(skier);
        registration = registrationRepository.save(registration);

        return convertToDTO(registration);
    }

    @Override
    public RegistrationDTO assignRegistrationToCourse(Long numRegistration, Long numCourse) {
        Registration registration = registrationRepository.findById(numRegistration).orElse(null);
        Course course = courseRepository.findById(numCourse).orElse(null);

        if (registration == null || course == null) {
            return null; // Ou gérer l'erreur selon votre logique
        }

        registration.setCourse(course);
        registration = registrationRepository.save(registration);

        return convertToDTO(registration);
    }

    @Transactional
    @Override
    public RegistrationDTO addRegistrationAndAssignToSkierAndCourse(RegistrationDTO registrationDTO, Long numSkieur, Long numCourse) {
        Skier skier = skierRepository.findById(numSkieur).orElse(null);
        Course course = courseRepository.findById(numCourse).orElse(null);

        if (skier == null || course == null) {
            return null;
        }

        Registration registration = convertToEntity(registrationDTO);
        registration.setSkier(skier);
        registration.setCourse(course);

        if (registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(
                registration.getNumWeek(), skier.getNumSkier(), course.getNumCourse()) >= 1) {
            log.info("Déjà inscrit pour cette semaine.");
            return null;
        }

        return convertToDTO(registrationRepository.save(registration));
    }

    // Méthode pour convertir un DTO en une entité Registration
    private Registration convertToEntity(RegistrationDTO dto) {
        Registration registration = new Registration();
        // Convertir la chaîne en entier si nécessaire
        registration.setNumWeek(Integer.parseInt(dto.getOtherField())); // Conversion de String à int
        return registration;
    }

    // Méthode pour convertir une entité Registration en DTO
    private RegistrationDTO convertToDTO(Registration registration) {
        RegistrationDTO dto = new RegistrationDTO();

        // Assurez-vous que skier et course ne sont pas null avant d'y accéder
        if (registration.getSkier() != null) {
            dto.setSkierId(registration.getSkier().getNumSkier());
        }
        if (registration.getCourse() != null) {
            dto.setCourseId(registration.getCourse().getNumCourse());
        }

        // Convertir l'entier en chaîne
        dto.setOtherField(String.valueOf(registration.getNumWeek()));
        return dto;
    }


    @Override
    public List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support) {
        return registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }
}