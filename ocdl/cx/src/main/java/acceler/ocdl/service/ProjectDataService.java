package acceler.ocdl.service;

import acceler.ocdl.entity.ProjectData;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectDataService {


    @Transactional
    ProjectData uploadProjectData();

    Page<ProjectData> getProjectData(ProjectData projectData, int page, int size);

    boolean batchDeleteProjectData(List<ProjectData> projectDatas);

    List<String> downloadProjectData(String refId);
}
