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
        String RELEASE_MODEL = "{algorithm}_v{release_version}.{cached_version}.{suffix}";
        String LATEST_MODEL = "{algorithm}_v{release_version}.{cached_version}";
    }

    interface PERSISTENCE {
//        String _BASE = "D://resources_cx/persistence";
        String ALGORITHMS = "/algorithms";
        String PROJECT = "/project";
        String USERS = "/users";
        String USER_ID_GENERATOR = "/user_id_generator";
        String NEW_MODEL = "/new_model";
        String REJECTED_MODELS = "/rejected_model";
    }

    interface IP {
        interface VIRTUAL {
            String MASTER = "10.8.0.1";
            String CPU = "10.8.0.6";
            String GPU = "172.31.24.77";
        }

        interface PUBLIC {
            String MASTER = "184.73.27.254";
            String CPU = "52.91.74.159";
            String GPU = "34.229.75.10";
        }
    }


    interface HDFS {
        String IP_ADDRESS = "hdfs://ec2-3-88-164-181.compute-1.amazonaws.com:9000";
        String USER_SPACE = "/UserSpace/";
        String USER_NAME = "hadoop";
    }

    interface KAFKA {
        String TOPIC = "mdmsg";
        String MESSAGE = "{publishedModelName} {modelUrl}";
        String KAFKA_URL = "ec2-35-171-163-170.compute-1.amazonaws.com:9092";
    }
}
