package sigma.training.eum.mapperInterface;

public interface EntityMapper <T, U>{
  U fromEntity(T entity);
  T toEntity(U domainObject);
}
