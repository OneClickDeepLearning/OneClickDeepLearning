package acceler.ocdl;

public interface CONSTANTS {

    interface APPLICATIONS_DIR {
        String USER_SPACE = "/home/hadoop/mount/UserSpace/";
//        String STAGE_SPACE = "/home/ec2-user/stage/";
        String STAGE_SPACE = "/Staging";
    }

    interface NAME_FORMAT {

        //TODO replace TestProject

        String USER_SPACE = "{userId}";
        String STAGED_MODEL = "{modelId}.{suffix}";

        String USER_ID = "{type}{userId}";
        String MODELDTO_VERSION = "v{release_version}.{cached_version}";
        String GIT_MODEL = "{algorithm}_v{release_version}.{cached_version}.{suffix}";
    }

    interface PERSISTENCE {
        String _BASE = "D://resources_cx/persistence";
        String ALGORITHMS = _BASE + "/algorithms";
        String PROJECT = _BASE + "/project";
        String USERS = _BASE + "/users";
        String USER_ID_GENERATOR = _BASE + "/user_id_generator";
        String NEW_MODEL = _BASE + "/new_model";
        String REJECTED_MODELS = _BASE + "/rejected_model";
    }

    interface IP {
        interface VIRTUAL {
            String MASTER = "172.31.24.77";
            String CPU = "172.31.24.77";
            String GPU = "172.31.24.77";
        }

        interface PUBLIC {
            String MASTER = "34.229.75.10";
            String CPU = "34.229.75.10";
            String GPU = "34.229.75.10";
        }
    }

    interface MACHINE {
        int GPU_AMOUNT = 1;
    }

    interface HDFS {
        String IP_ADDRESS = "hdfs://66.131.186.246:9000";
        String USER_SPACE = "/UserSpace/";
        String USER_NAME = "hadoop";
    }

    interface KAFKA {
        String TOPIC = "mdmsg";
        String MESSAGE = "{publishedModelName} {modelUrl}";
        String KAFKA_URL = "ec2-35-171-163-170.compute-1.amazonaws.com:9092";
    }
}
