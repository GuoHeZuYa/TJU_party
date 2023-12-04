package edu.twt.party.pojo.userProcess;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserProcessNode {
    APPLICANT_APPLICATION(1,"入党申请书"),
    APPLICANT_STUDY_GROUP(2,"入党申请人学习小组"),
    APPLICANT_GROUP_SELECTION(3,"入党申请人团推优"),
    APPLICANT_EXERCISE(4,"入党申请人网上培训"),
    APPLICANT_EXAMINATION(5,"入党申请人会审备案"),

    ACTIVIST_TRAIN(6,"积极分子院级培训"),
    ACTIVIST_CONTACTOR(7,"积极分子确定联系人"),
    ACTIVIST_BRANCH_EXAMINATION(8,"积极分子支部考察"),
    ACTIVIST_PARTY_MEMBER_OPINION(9,"积极分子听取党员意见"),
    ACTIVIST_PEOPLE_OPINION(10,"积极分子听取群众意见"),
    ACTIVIST_GROUP_CONTACTOR_OPINION(11,"积极分子党小组联系人意见"),
    ACTIVIST_EXAMINATION(12,"积极分子支部会讨论上报备案"),

    DEVELOP_INTRODUCER(13,"发展对象确定两名介绍人"),
    DEVELOP_AUTOBIOGRAPHY(14,"发展对象个人自传政审"),
    DEVELOP_PARTY_SCHOOL(15,"发展对象党校培训"),
    DEVELOP_MATERIAL(16,"发展对象材料齐全"),
    DEVELOP_EXAMINATION(17,"发展对象综合审查"),
    DEVELOP_PUBLISH(18,"发展对象预审公示"),
    DEVELOP_APPLICATION(19,"发展对象入党申请书"),
    DEVELOP_CONFERENCE(20,"发展对象发展大会"),
    DEVELOP_COMMUNICATION(21,"发展对象党委谈话审批"),
    DEVELOP_RECORD(22,"发展对象上级备案"),

    PROBATIONARY_OATH(23,"预备党员入党宣誓"),
    PROBATIONARY_EXERCISE(24,"预备党员培训"),
    PROBATIONARY_ORGANIZATIONAL_LIFE(25,"预备党员参加组织生活"),
    PROBATIONARY_APPLICATION(26,"预备党员转正申请"),
    PROBATIONARY_OPINION(27,"预备党员党员群众意见"),
    PROBATIONARY_GROUP_CONTACTOR_OPINION(28,"预备党员党小组介绍人意见"),
    PROBATIONARY_PUBLISH(29,"预备党员转正公示"),
    PROBATIONARY_CONFERENCE(30,"预备党员转正大会"),
    PROBATIONARY_APPROVE(31,"预备党员党委审批"),
    PROBATIONARY_COMMUNICATION(32,"预备党员书记谈话");

    @EnumValue
    private final int id;
    @JsonValue
    private final String name;
}
