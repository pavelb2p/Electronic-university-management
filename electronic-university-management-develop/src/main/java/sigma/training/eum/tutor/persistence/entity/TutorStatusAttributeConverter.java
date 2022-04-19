package sigma.training.eum.tutor.persistence.entity;

import sigma.training.eum.tutor.dictionary.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Locale;

@Converter
public class TutorStatusAttributeConverter implements AttributeConverter<Status, String> {
  @Override
  public String convertToDatabaseColumn(Status status) {
    return status.toString().toLowerCase(Locale.ROOT);
  }

  @Override
  public Status convertToEntityAttribute(String status) {
    return Status.valueOf(status.toUpperCase(Locale.ROOT));
  }
}

