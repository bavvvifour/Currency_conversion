package dao;

import java.sql.SQLException;
import java.util.List;

public interface IDAO<T> {
    void create(T t) throws SQLException;

    List<T> getAll() throws SQLException;

    T getByCode(String code) throws SQLException;

    void update(T t) throws SQLException;

    boolean removeById(Long id);

    boolean removeAll();
}
