package hotel.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

public interface GenericDao<T, PK extends Serializable> {
	T save(T entity);
	void saveAll(Collection<T> entities);
	T update(T entity);
	void delete(PK id);
	Optional<T> findById(PK id);
	Long countALl();
}
