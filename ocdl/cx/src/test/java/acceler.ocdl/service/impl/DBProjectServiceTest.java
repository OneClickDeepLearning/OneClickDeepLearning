package acceler.ocdl.service.impl;

import acceler.ocdl.dao.ProjectDao;
import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.User;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.service.ProjectService;
import acceler.ocdl.utils.TimeUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class DBProjectServiceTest {

    @TestConfiguration
    static class DBProjectServiceTestContextConfiguration {

        @Bean
        public ProjectService projectService() {
            return new DBProjectService();
        }

//        @Bean
//        public TimeUtil timeUtil() { return new TimeUtil(); }
    }

    @Autowired
    private ProjectService projectService;

    @MockBean
    private ProjectDao projectDao;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private Project p1;
    private Project p2;
    private Project p3;
    private User user;

    @Before
    public void setUp() {

        user = User.builder()
                .id(1L)
                .userName("ivy")
                .password("123456")
                .build();

        // with id = 1 & isDeleted = false
        p1 = Project.builder()
                .id(1L)
                .name("delete-false")
                .description("with id = 1 & isDeleted = false")
                .isDeleted(false)
                .createdAt(TimeUtil.currentTimeStampStr())
                .build();


        // with id = 2 & isDeleted = true
        p2 = Project.builder()
                .id(2L)
                .name("create")
                .description("one click deep learning")
                .isDeleted(false)
                .createdAt(TimeUtil.currentTimeStampStr())
                .build();

        // without id
        p3 = Project.builder()
                .name("create")
                .description("one click deep learning")
                .build();
    }

    @Test
    public void testGetProject() {

        // test1: get project without exceptions
        Mockito.when(projectDao.findByIdAndIsDeletedIsFalse(1L))
                .thenReturn(Optional.ofNullable(p1));
        Project project = projectService.getProject(p1.getId());

        // then
        assertEquals(project.getId(), p1.getId());
        assertEquals(project.getName(), p1.getName());
        assertEquals(project.getDescription(), p1.getDescription());
        assertEquals(project.getIsDeleted(), p1.getIsDeleted());
        assertEquals(project.getCreatedAt(), p1.getCreatedAt());

        // test2: delete project and throw exception
        Long nonExistedId = 3L;
        Mockito.when(projectDao.findByIdAndIsDeletedIsFalse(nonExistedId))
                .thenReturn(Optional.ofNullable(null));

        expectedEx.expect(NotFoundException.class);
        expectedEx.expectMessage(String.format("Fail to find Project(# %d )",nonExistedId));
        project = projectService.getProject(nonExistedId);

    }

    @Test
    public void testDeleteProject() {

        // test1: delete project without exceptions
        Mockito.when(projectDao.findByIdAndIsDeletedIsFalse(1L))
                .thenReturn(Optional.ofNullable(p1));

        // with id = 1 & isDeleted = true
        Project deletedP1 = Project.builder()
                .id(1L)
                .name("delete-true")
                .description("with id = 1 & isDeleted = true")
                .isDeleted(true)
                .createdAt("1234567890")
                .deletedAt(TimeUtil.currentTimeStampStr())
                .build();

        Mockito.when(projectDao.save(deletedP1))
                .thenReturn(deletedP1);
        boolean success = projectService.deleteProject(p1.getId());

        assertEquals(true, success);

        // test2: delete project and throw the exceptions
        Long nonExistedId = 3L;
        Mockito.when(projectDao.findByIdAndIsDeletedIsFalse(nonExistedId))
                .thenReturn(Optional.ofNullable(null));

        expectedEx.expect(NotFoundException.class);
        expectedEx.expectMessage(String.format("Fail to find Project(# %d )",nonExistedId));
        projectService.deleteProject(nonExistedId);
    }

    @Test
    public void testSaveProject() {

        // test1: create project
        Mockito.when(projectDao.save(p3))
                .thenReturn(p2);

        Project project = projectService.saveProject(p3, user);
        assertEquals(true, project.getId() != null);
        assertEquals(p2. getName(), project.getName());
        assertEquals(p2.getDescription(), project.getDescription());
        assertEquals(false, project.getIsDeleted());
        assertEquals(true, project.getCreatedAt()!= null);


        // test2: update project
        Mockito.when(projectDao.findById(p2.getId()))
                .thenReturn(Optional.ofNullable(p2));

        p2.setName("update");
        Mockito.when(projectDao.save(p2))
                .thenReturn(p2);

        project = projectService.saveProject(p2, user);
        assertEquals(true, project.getId() != null);
        assertEquals("update", project.getName());
        assertEquals(p2.getDescription(), project.getDescription());
        assertEquals(false, project.getIsDeleted());
        assertEquals(true, project.getCreatedAt()!= null);

    }


}
