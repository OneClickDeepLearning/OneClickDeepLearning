package acceler.ocdl.service.impl;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({NewModel.class, AbstractUser.class, RejectedModel.class, Algorithm.class})
public class DefaultModelServiceImplTest {

    @InjectMocks
    DefaultModelServiceImpl modelServiceImpl;

    @Before
    public void setUp() {

        Date date = new Date();
        long userId = 1004L;
        //mock user
        InnerUser user = new InnerUser();
        user.setUserName("ivy");
        user.setPassword("1234");
        user.setRole(AbstractUser.Role.DEVELOPER);

        PowerMockito.mockStatic(AbstractUser.class);
        PowerMockito.when(AbstractUser.findUserById(userId)).thenReturn(user);

        // mock new models
        NewModel newModel1 = new NewModel();
        newModel1.setModelId(9L);
        newModel1.setOwnerId(userId);
        newModel1.setName("new model 1");
        newModel1.setStatus(Model.Status.NEW);
        newModel1.setComments("");
        newModel1.setSuffix(".model");
        newModel1.setCommitTime(date);

        NewModel newModel2 = new NewModel();
        newModel2.setModelId(7L);
        newModel2.setOwnerId(userId);
        newModel2.setName("new model 2");
        newModel2.setStatus(Model.Status.NEW);
        newModel2.setComments("");
        newModel2.setSuffix(".model");
        newModel2.setCommitTime(new Date(date.getTime() - (long)2 * 24 * 60 * 60 * 1000));

        NewModel[] newModels = new NewModel[]{newModel1, newModel2};

        PowerMockito.mockStatic(NewModel.class);
        PowerMockito.when(NewModel.getAllNewModelsByUser(userId)).thenReturn(newModels);

        //mock rejected model
        RejectedModel rejectedModel1 = new RejectedModel();
        rejectedModel1.setModelId(8L);
        rejectedModel1.setOwnerId(userId);
        rejectedModel1.setName("rejected model");
        rejectedModel1.setStatus(Model.Status.REJECTED);
        rejectedModel1.setComments("Duplicate model");
        rejectedModel1.setSuffix(".tflite");
        rejectedModel1.setRejectedTime(new Date(date.getTime() - (long)5 * 24 * 60 * 60 * 1000));

        RejectedModel[] rejectedModels = new RejectedModel[]{rejectedModel1};
        PowerMockito.mockStatic(RejectedModel.class);
        PowerMockito.when(RejectedModel.getAllRejectedModelsByUser(userId)).thenReturn(rejectedModels);

        // approved model
        ApprovedModel approvedModel1 = new ApprovedModel();
        approvedModel1.setModelId(6L);
        approvedModel1.setOwnerId(userId);
        approvedModel1.setName("approved model 1");
        approvedModel1.setStatus(Model.Status.APPROVED);
        approvedModel1.setComments("Good!");
        approvedModel1.setSuffix(".model");
        approvedModel1.setCachedVersion(1L);
        approvedModel1.setReleasedVersion(2L);
        approvedModel1.setApprovedTime(new Date(date.getTime() - (long)1 * 24 * 60 * 60 * 1000));

        ApprovedModel[] approvedModels = new ApprovedModel[]{approvedModel1};


        Algorithm algorithm1 = new Algorithm();
        algorithm1.setAlgorithmName("NLP");

        Map<String, Model[]> algorithmMap = new HashMap<>();
        algorithmMap.put("NLP", approvedModels);
        PowerMockito.mockStatic(Algorithm.class);
        PowerMockito.when(Algorithm.getAlgorithmOfApprovedModel(approvedModel1)).thenReturn(algorithm1);
        PowerMockito.when(Algorithm.getAllAlgorithmAndModels()).thenReturn(algorithmMap);

    }

    @Test
    public void getModelListByUser() {

        long userId = 1004L;
        int size = 4;
        ModelDto[] models = modelServiceImpl.getModelListByUser(userId);
        Assert.assertEquals(models.length, size);
        Assert.assertEquals(models[0].getModelName(),"new model 1");
        Assert.assertEquals(models[1].getModelName(),"approved model 1");
        Assert.assertEquals(models[2].getModelName(),"new model 2");
        Assert.assertEquals(models[3].getModelName(),"rejected model");

        System.out.println("new model 1:" + models[0].getTimeStamp());
        System.out.println("approved model 1:" + models[1].getTimeStamp());
        System.out.println("new model 2:" + models[2].getTimeStamp());
        System.out.println("rejected model:" + models[3].getTimeStamp());
    }




}