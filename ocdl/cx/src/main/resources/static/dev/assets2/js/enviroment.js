const enviorment = {
    API:{
        /*------ Jupyter service resource---------*/
        JUPYTER_SERVER: '/rest/container/',

        /*------ Auth service resource---------*/
        LOGIN: '/rest/auth/login',
        LOGOUT: '/rest/auth/logout',
        REGISTER: '/rest/auth/register',

        /*------ Template service resource---------*/
        TEMPLATE_LIST: '/rest/template/names',
        TEMPLATE_CODE: '/template/templates"',

        /*------ Model service resource---------*/
        MODEL:'/rest/models',
        MODEL_PUSH: '/rest/model',
        MODEL_TYPE: '/rest/models/modeltypes',

        /*------ Configure service resource---------*/
        PROJECT: '/rest/projects',
        PROJECT_NAME: '/rest/project/name',
        PROJECT_UPDATE: '/rest/projects/config'
    }
};