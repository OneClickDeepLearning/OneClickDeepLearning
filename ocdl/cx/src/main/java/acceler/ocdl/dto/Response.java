package acceler.ocdl.dto;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private Object data;

    private Response() {
    }


    public static Response.Builder getBuilder() {
        Response resp = new Response();
        Response.Builder respBuilder = new Response.Builder(resp);
        return respBuilder;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public static class Builder {
        private Response resp;

        Builder(Response resp) {
            this.resp = resp;
        }

        public Builder setCode(int code) {
            this.resp.code = code;
            return this;
        }

        public Builder setMessage(String msg) {
            this.resp.message = msg;
            return this;
        }


        public Builder setData(final Object data) {
            this.resp.data = data;
            return this;
        }

        public Response build() {
            if (this.resp.code == Code.SUCCESS && this.resp.message == null){
                this.resp.message = "success";
            }
            return resp;
        }
    }


    public class Code {
        public static final int SUCCESS = 200;
        public static final int ERROR = 400;
        public static final int UNAUTH = 403;
    }
}
