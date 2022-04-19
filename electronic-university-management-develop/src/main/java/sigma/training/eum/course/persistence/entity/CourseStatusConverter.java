package sigma.training.eum.course.persistence.entity;
import sigma.training.eum.course.dictionary.Status;

import javax.persistence.AttributeConverter;
import java.util.Locale;

public class CourseStatusConverter implements AttributeConverter<Status, String> {

  @Override
  public String convertToDatabaseColumn(Status status) {
    return status.toString().toLowerCase(Locale.ROOT);
  }

  @Override
  public Status convertToEntityAttribute(String dbData) {
    return Status.valueOf(dbData.toUpperCase(Locale.ROOT));
  }
}
