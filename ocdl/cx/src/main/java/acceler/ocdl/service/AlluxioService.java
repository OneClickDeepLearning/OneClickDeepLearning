package acceler.ocdl.service;

import acceler.ocdl.exception.AlluxioException;

public interface AlluxioService {

    public void downloadFromStaging(String fileName, Long userId) throws AlluxioException;
}
