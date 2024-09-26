package com.github.hiwepy.dapp.bo;

import com.github.hiwepy.dapp.strategy.ChainPlatform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChainSignBO<T> {

    /**
     * 区块链平台
     */
    private ChainPlatform platform;

    //==============================账号信息====================================

    /**
     * 登录账户id
     */
    private Long accountId;
    /**
     * 登录账号
     */
    private String account;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 加密后的登录密码
     */
    private String encodePassword;
    /**
     * 账号状态（0:禁用|1:可用|2:锁定|3:密码过期）
     */
    private Integer status;

    //==============================基本信息====================================

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户号
     */
    private String userCode;
    /**
     * 别名（昵称）
     */
    private String nickname;
    /**
     * 头像：图片路径或图标样式
     */
    private String avatar;
    /**
     * 身份类型 1 身份证 2护照 3港澳 4台湾 5 外国人居留证
     */
    private String idType;
    /**
     * 证件号码
     */
    private String idCard;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 性别（0：未知项、1：男、2：女）
     */
    private Integer gender;
    /**
     * 出生日期
     */
    private Long birthday;
    /**
     * 用户年龄
     */
    private Integer age;
    /**
     * 用户位置：常驻国家/地区编码
     */
    private String regionCode;
    /**
     * 用户位置：常驻国家/地区名称
     */
    private String country;
    /**
     * 用户位置：常驻省份
     */
    private String province;
    /**
     * 用户位置：常驻城市
     */
    private String city;
    /**
     * 用户位置：常驻区域
     */
    private String area;
    /**
     * 用户位置：常驻地经度
     */
    private Double longitude;
    /**
     * 用户位置：常驻地纬度
     */
    private Double latitude;
    /**
     * 官方语言
     */
    private String lang;
    /**
     * 时区
     */
    private String zone;
    /**
     * 学校代码
     */
    private String xxdm;

    //==============================权限信息====================================

    /**
     * 权限信息：直属组织机构ID（学校组织机构ID）
     */
    private Long orgId;
    /**
     * 权限信息：校区组织机构ID
     */
    private Long xqOrgId;
    /**
     * 权限信息：当前使用的角色ID
     */
    private Long roleId;
    /**
     * <p>权限信息：身份类型（使用时需要根据数字对应的身份类型进行本地系统转换） </p>
     * <ul>
     * <li>业务中台 - 身份类型（1：教职工、2：学生、3：家长、4：开发者、5：普通用户、6：管理员、7：临聘人员、8：外聘人员） </li>
     * <li>本地系统 - 身份类型（1:系统管理员、2:学校管理员、3:老师、4:学生、5:家长、6:开发者、7:访客）</li>
     * </ul>
     */
    private Integer identityId;
    /**
     * 权限信息：教师id、学生id、家长id
     */
    private Long infoId;

    //==============================请求来源====================================
    /**
     * 请求来源：应用ID
     */
    private String appId;
    /**
     * 请求来源：应用渠道编码
     */
    private String appChannel;
    /**
     * 请求来源：应用版本号
     */
    private String appVersion;
    /**
     * 请求来源：IP地址
     */
    private String ipAddress;
    /**
     * 请求来源：平台名称，如：huawei、xiaomi、oppo、vivo、apple、meizu、samsung
     */
    private String paltform;
    /**
     * 请求来源：设备唯一标识
     */
    private String devId;
    /**
     * 请求来源：设备名称
     */
    private String device;

    //==============================辅助信息====================================

    /**
     * 辅助信息：登录时间戳
     */
    private Long loginTime;
    /**
     * 辅助信息：用户是否绑定信息
     */
    private Boolean bound = Boolean.FALSE;
    /**
     * 辅助信息：用户是否完善信息
     */
    private Boolean initial = Boolean.FALSE;
    /**
     * 辅助信息：用户是否需要多因子验证
     */
    private Boolean verify = Boolean.FALSE;
    /**
     * 辅助信息：是否切换角色登录
     */
    private Boolean runAs = Boolean.FALSE;

    private T param;

}
