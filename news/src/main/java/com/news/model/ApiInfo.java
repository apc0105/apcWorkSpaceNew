package com.news.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/9.
 */
public class ApiInfo implements Serializable {

    //包类型
    private String packageType;
    //包名
    private String packageName;
    //包所在相对地址
    private String packageRelativeAddress;
    //包版本
    private String packageVersion;
    //Api文档名
    private String docName;
    //Api文档相对地址
    private String docRelativeAddress;
    //是否依赖包
    private boolean isDependencyPackage;

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageRelativeAddress() {
        return packageRelativeAddress;
    }

    public void setPackageRelativeAddress(String packageRelativeAddress) {
        this.packageRelativeAddress = packageRelativeAddress;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocRelativeAddress() {
        return docRelativeAddress;
    }

    public void setDocRelativeAddress(String docRelativeAddress) {
        this.docRelativeAddress = docRelativeAddress;
    }

    public boolean isDependencyPackage() {
        return isDependencyPackage;
    }

    public void setDependencyPackage(boolean dependencyPackage) {
        isDependencyPackage = dependencyPackage;
    }
}
