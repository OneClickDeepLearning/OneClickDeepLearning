const enviorment = {
    API:{
        /*------ Jupyter service resource---------*/
        JUPYTER_SERVER: '/rest/container/type/',
        DELETE_SERVER: '/rest/container',

        /*------ Auth service resource---------*/
        LOGIN: '/rest/auth/login',
        LOGOUT: '/rest/auth/logout',
        REGISTER: '/rest/auth/register',

        /*------ Template service resource---------*/
        TEMPLATE_LIST: '/rest/template/file',
        TEMPLATE_CODE: '/rest/template/code',

        /*------ Model service resource---------*/
        MODEL:'/rest/model',
        MODEL_PUSH: '/rest/model',
        MODEL_TYPE: '/rest/project/algorithm',

        /*------ Configure service resource---------*/
        PROJECT: '/rest/project/config',
        PROJECT_NAME: '/rest/project/name',
        PROJECT_UPDATE: '/rest/project/config',

        /* ------- Upload  ---------*/
        UPLOAD:'/rest/project/data'
    }
};