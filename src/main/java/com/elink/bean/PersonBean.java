package com.elink.bean;

/**
 * @Description：人员实体类
 * @Author： Evloution_
 * @Date： 2019/9/29
 * @Email： 15227318030@163.com
 */
public class PersonBean {
    private String personName;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Override
    public String toString() {
        return "PersonBean{" +
                "personName='" + personName + '\'' +
                '}';
    }
}
