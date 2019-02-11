package ch.moviescore.core.data.uploadFile;


import ch.moviescore.core.data.DaoInterface;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-db-web
 * @package ch.wetwer.moviedbapi.data.uploadFile
 * @created 16.01.2019
 **/

@Service
public class UploadFileDao implements DaoInterface<UploadFile> {

    private UploadFileRepository uploadFileRepository;

    public UploadFileDao(UploadFileRepository uploadFileRepository) {
        this.uploadFileRepository = uploadFileRepository;
    }

    @Override
    public UploadFile getById(Long id) {
        return uploadFileRepository.getOne(id);
    }

    @Override
    public List<UploadFile> getAll() {
        return uploadFileRepository.findAllByOrderByVideoTypeDescFilenameAsc();
    }

    @Override
    public void save(UploadFile uploadFile) {
        try {
            uploadFileRepository.save(uploadFile);
        } catch (Exception ignored) {
        }
    }

    public UploadFile getByHash(int hash) {
        return uploadFileRepository.findByHash(hash);
    }

    public void delete(UploadFile uploadFile) {
        uploadFileRepository.delete(uploadFile);
    }

    public boolean exists(int hashCode) {
        try {
            return uploadFileRepository.existsById(getByHash(hashCode).getId());
        } catch (NullPointerException e) {
            return false;
        }
    }
}
