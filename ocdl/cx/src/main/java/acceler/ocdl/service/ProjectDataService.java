package acceler.ocdl.service;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.ProjectData;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectDataService {


    @Transactional
    ProjectData uploadProjectData(Project project, String srcPath);

    Page<ProjectData> getProjectData(ProjectData projectData, int page, int size);

    boolean batchDeleteProjectData(List<ProjectData> projectDatas, Project project);

    boolean downloadProjectData(String refId, Project project);
}
