package ch.moviescore.core.data.importlog;

import ch.moviescore.core.data.DaoInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportLogDao implements DaoInterface<ImportLog> {

    private ImportLogRepository importLogRepository;

    public ImportLogDao(ImportLogRepository importLogRepository) {
        this.importLogRepository = importLogRepository;
    }

    @Override
    public ImportLog getById(Long id) {
        return importLogRepository.getOne(id);
    }

    @Override
    public List<ImportLog> getAll() {
        return importLogRepository.findAllByOrderByTimestampDesc();
    }

    @Override
    public void save(ImportLog importLog) {
        importLogRepository.save(importLog);
    }

    public void delete() {
        importLogRepository.deleteAll();
    }
}
