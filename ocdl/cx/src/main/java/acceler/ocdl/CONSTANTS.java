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
        String USER_ID = "{type}{userId}";

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

    interface IP {
        interface VIRTUAL {
            String MASTER = "10.8.0.1";
            String CPU  = "10.8.0.6";
            String GPU = "10.8.0.10";
        }

        interface PUBLIC{
            String MASTER = "3.89.28.106";
            String CPU  = "3.87.64.159";
            String GPU = "66.131.186.246";
        }
    }

    interface MACHINE{
        int GPU_AMOUNT = 1;
    }

    interface HADOOPMASTER{
        String ADDRESS = "hdfs://10.8.0.14:9000";
        String USERDIR = "/UserSpace/";
    }
}
