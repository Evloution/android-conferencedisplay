package com.elink.bean;

import com.elink.utils.FileUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * @Description：会议实体类
 * @Author： Evloution_
 * @Date： 2019/10/13
 * @Email： 15227318030@163.com
 */
public class MeetingBean {
    private String meetingName; // 会议名称
    private int meetingStatus; // 会议状态
    private String createTime; // 会议创建时间
    private String startTime; // 会议开始时间
    private String endTime; // 会议结束时间
    private List<AgendaBean> agendaBeanList;

    public MeetingBean(String meetingName, List<AgendaBean> agendaBeanList) {
        this.meetingName = meetingName;
        this.agendaBeanList = agendaBeanList;
    }

    public void save() {
        Gson gson = new Gson();
        FileUtil.writeTxtToFile(gson.toJson(this).toString(), "/sdcard/ConferenceRecords/", meetingName + ".txt");
    }

    public List<AgendaBean> getAgendaBeanList() {
        return agendaBeanList;
    }

    public void setAgendaBeanList(List<AgendaBean> agendaBeanList) {
        this.agendaBeanList = agendaBeanList;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
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

    public int getMeetingStatus() {
        return meetingStatus;
    }

    public void setMeetingStatus(int meetingStatus) {
        this.meetingStatus = meetingStatus;
    }

    @Override
    public String toString() {
        return "MeetingBean{" +
                "meetingName='" + meetingName + '\'' +
                ", meetingStatus=" + meetingStatus +
                ", createTime='" + createTime + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", agendaBeanList=" + agendaBeanList +
                '}';
    }
}
