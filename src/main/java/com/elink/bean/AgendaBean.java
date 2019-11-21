package com.elink.bean;

import java.util.List;

/**
 * @Description：议程实体类
 * @Author： Evloution_
 * @Date： 2019/10/12
 * @Email： 15227318030@163.com
 */
public class AgendaBean {
    private String name; // 议程名称
    private String departmentName; // 科室名称
    private int agendaStatus; // 议程状态
    private String startTime; // 开始时间
    private String endTime; // 结束时间
    private int orderCode; // 顺序编号
    private boolean isDisplayPerson;//是否显示人员
    private List<PersonBean> personList; // 人员列表

    public AgendaBean(String name, List<PersonBean> personList) {
        this.name = name;
        this.personList = personList;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public boolean getDisplayPerson() {
        return isDisplayPerson;
    }

    public void setDisplayPerson(boolean displayPerson) {
        isDisplayPerson = displayPerson;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<PersonBean> getPersonList() {
        return personList;
    }

    public void setPersonList(List<PersonBean> personList) {
        this.personList = personList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAgendaStatus() {
        return agendaStatus;
    }

    public void setAgendaStatus(int agendaStatus) {
        this.agendaStatus = agendaStatus;
    }

    public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }

    @Override
    public String toString() {
        return "AgendaBean{" +
                "name='" + name + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", agendaStatus=" + agendaStatus +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", orderCode=" + orderCode +
                ", isDisplayPerson=" + isDisplayPerson +
                ", personList=" + personList +
                '}';
    }
}
