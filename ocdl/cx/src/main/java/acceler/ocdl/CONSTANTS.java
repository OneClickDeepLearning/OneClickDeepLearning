package acceler.ocdl;

public interface CONSTANTS {

    interface APPLICATIONS_DIR {
        String USER_SPACE = "/home/hadoop/nfs_hdfs/UserSpace/";
        String STAGE_SPACE = "/home/ec2-user/stage/";
        String GIT_REPO_SPACE = "/home/ec2-user/models/";
    }

    interface NAME_FORMAT {
        String USER_SPACE = "{projectName}-{userId}";
        String STAGED_MODEL = "{fileName}-{timestamp}";

        interface MODEL_FILE {
            String NEW_MODEL = "{modelName}_{commitTime}";
            String REJECTED_MODEL = "{modelName}_{rejectedTime}";
            String APPROVED_MODEL = "{modelName}_{approvedTime}_{algorithm}_{releaseVersion}_{cachedVersion}";
        }
    }

    interface PERSISTENCE {
        String _BASE = "./resource/persistence";
        String ALGORITHMS = _BASE + "/algorithms";
        String PROJECT = _BASE + "/project";
        String USERS = _BASE + "/users";
    }
}
