package sigma.training.eum.mapperInterface;

public interface DtoMapper<T, U>{
  U fromDto(T dto);
  T toDto(U domainObject);
}
