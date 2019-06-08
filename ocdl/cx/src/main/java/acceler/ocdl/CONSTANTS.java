package acceler.ocdl;

public interface CONSTANTS {

    interface APPLICATIONS_DIR {
        String USER_SPACE = "/home/hadoop/mount/UserSpace/";
//        String STAGE_SPACE = "/home/ec2-user/stage/";
        String STAGE_SPACE = "/Staging";
        String GIT_REPO_SPACE = "/home/ec2-user/models/";
    }

    interface NAME_FORMAT {

        //TODO replace TestProject

        String USER_SPACE = "testproject-{userId}";
        String STAGED_MODEL = "{modelId}.{suffix}";

        String USER_ID = "{type}{userId}";
        String MODELDTO_VERSION = "v{release_version}.{cached_version}";
        String GIT_MODEL = "{algorithm}_v{release_version}.{cached_version}.{suffix}";
    }

    interface PERSISTENCE {
        String _BASE = "/resources_cx/persistence";
        String ALGORITHMS = _BASE + "/algorithms";
        String PROJECT = _BASE + "/project";
        String USERS = _BASE + "/users";
        String NEW_MODEL = _BASE + "/new_model";
        String REJECTED_MODELS = _BASE + "/rejected_model";
    }

    interface IP {
        interface VIRTUAL {
            String MASTER = "10.8.0.1";
            String CPU = "10.8.0.6";
            String GPU = "10.8.0.10";
        }

        interface PUBLIC {
            String MASTER = "3.89.28.106";
            String CPU = "3.87.64.159";
            String GPU = "66.131.186.246";
        }
    }

    interface MACHINE {
        int GPU_AMOUNT = 1;
    }

    interface HDFS {
        String IP_ADDRESS = "hdfs://10.8.0.14:9000";
        String USER_SPACE = "/UserSpace/";
        String USER_NAME = "hadoop";
    }

    interface KAFKA {
        String TOPIC = "mdmsg";
        String MESSAGE = "{publishedModelName} {modelUrl}";
    }
}
