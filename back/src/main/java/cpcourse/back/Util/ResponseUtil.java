package cpcourse.back.Util;

public class ResponseUtil {
    public static ResponseEntity success(Object data){
        ResponseEntity responseEntity = new ResponseEntity(200, null, data);
        return responseEntity;
    }

    //当成功且无返回参数时使用
    public static ResponseEntity success(){
        return success(null);
    }

    public static ResponseEntity error(Integer status, String msg){
        ResponseEntity responseEntity = new ResponseEntity(status, msg, null);
        return responseEntity;
    }

    public static ResponseEntity error(Integer status, String msg, Object data){
        ResponseEntity responseEntity = new ResponseEntity(status, msg, data);
        return responseEntity;
    }
}
