package sigma.training.eum.student.persistence.entity;


import sigma.training.eum.student.dictionary.Status;

import javax.persistence.AttributeConverter;
import java.util.Locale;

public class StudentStatusConverter implements AttributeConverter<Status, String> {
  @Override
  public String convertToDatabaseColumn(Status status) {
    return status.toString().toLowerCase(Locale.ROOT);
  }

  @Override
  public Status convertToEntityAttribute(String dbData) {
    return Status.valueOf(dbData.toUpperCase(Locale.ROOT));
  }
}
