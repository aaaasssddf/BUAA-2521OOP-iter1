import repository.CourseRepository;
import repository.EnrollmentRepository;
import repository.UserRepository;
import service.CourseService;
import service.SessionService;
import service.UserService;

public class ACPSystem {

    private final UserRepository userRepository = new UserRepository();
    private final CourseRepository courseRepository = new CourseRepository();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepository();

    private final SessionService sessionService = new SessionService(userRepository);
    private final UserService userService =
            new UserService(userRepository, sessionService);
    private final CourseService courseService =
            new CourseService(courseRepository, sessionService, userRepository, enrollmentRepository);


    private final CommandProcessor commandProcessor =
            new CommandProcessor(userService, courseService, sessionService);

    private static final ACPSystem Instance = new ACPSystem();

    public ACPSystem() {

    }

    public CommandProcessor getCommandProcessor() {
        return commandProcessor;
    }

    public static ACPSystem getInstance() {
        return Instance;
    }
}
