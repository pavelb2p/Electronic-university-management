package sigma.training.eum.user.persistence.entity;

import sigma.training.eum.user.dictionary.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Locale;

@Converter
public class EntityRoleAttributeConverter implements AttributeConverter<Role, String> {
  @Override
  public String convertToDatabaseColumn(Role attribute) {
    return attribute.toString().toLowerCase(Locale.ROOT);
  }

  @Override
  public Role convertToEntityAttribute(String dbData) {
    return Role.valueOf(dbData.toUpperCase(Locale.ROOT));
  }
}
