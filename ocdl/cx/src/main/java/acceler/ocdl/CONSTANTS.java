package acceler.ocdl;

public interface CONSTANTS {

    interface APPLICATIONS_DIR {
        String USER_SPACE = "/home/hadoop/nfs_hdfs/UserSpace/";
        String STAGE_SPACE = "/home/ec2-user/stage/";
        String GIT_REPO_SPACE = "/home/ec2-user/models/";
    }

    interface NAME_FORMAT {
        String USER_SPACE = "{projectName}-{userId}";
        String STAGED_MODEL = "{FileName}-{timestamp}";
    }


}
