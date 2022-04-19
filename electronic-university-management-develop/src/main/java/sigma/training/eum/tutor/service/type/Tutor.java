package sigma.training.eum.tutor.service.type;


import sigma.training.eum.tutor.dictionary.Status;
import sigma.training.eum.user.service.type.UserId;

public record Tutor(TutorId id, Status status, String name, UserId userId) {

}
