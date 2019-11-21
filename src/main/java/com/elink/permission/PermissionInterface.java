package com.elink.permission;

/**
 * @author Evloution_
 * @date 2018/12/5
 * @explain
 */
public interface PermissionInterface {
    /**
     * 可设置请求权限请求码
     */
    int getPermissionsRequestCode();

    /**
     * 设置需要请求的权限
     */
    String[] getPermissions();

    /**
     * 请求权限成功回调
     */
    void requestPermissionsSuccess();

    /**
     * 请求权限失败回调
     */
    void requestPermissionsFail();
}
