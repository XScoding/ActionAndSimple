package com.xs.demo;

/**
 * Created by xs code on 2019/3/27.
 */

public class LBean {


    private String code;
    private DataEntity data;
    private String message;

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public class DataEntity {

        private String phone;
        private String name;
        private String userId;;

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPhone() {
            return phone;
        }

        public String getName() {
            return name;
        }

        public String getUserId() {
            return userId;
        }

    }
}
