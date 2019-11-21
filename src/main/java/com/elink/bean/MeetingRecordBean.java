package com.elink.bean;

import android.os.Bundle;

import com.elink.R;

/**
 * @Description：会议记录实体类
 * @Author： Evloution_
 * @Date： 2019/10/12
 * @Email： 15227318030@163.com
 */
public class MeetingRecordBean {
    private String meetingName; // 会议名称
    private String meetingTime; // 会议时间
    private String meetingStatus; // 会议状态

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingStatus() {
        return meetingStatus;
    }

    public void setMeetingStatus(String meetingStatus) {
        this.meetingStatus = meetingStatus;
    }

    @Override
    public String toString() {
        return "MeetingRecordBean{" +
                "meetingName='" + meetingName + '\'' +
                ", meetingTime='" + meetingTime + '\'' +
                ", meetingStatus='" + meetingStatus + '\'' +
                '}';
    }
}
