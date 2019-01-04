package ch.moviescore.core.data;

import java.util.List;

public interface DaoInterface<T> {

    T getById(Long id);

    List<T> getAll();

    void save(T t);

}
