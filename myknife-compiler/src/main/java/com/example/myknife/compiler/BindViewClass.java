package com.example.myknife.compiler;

public class BindViewClass {
    public String field;
    public int id;
    public String outerClass;
    public String className;
    public String pkgName;

    @Override
    public String toString() {
        return "BindViewClass{" +
                "field='" + field + '\'' +
                ", id=" + id +
                ", outerClass='" + outerClass + '\'' +
                ", className='" + className + '\'' +
                ", pkgName='" + pkgName + '\'' +
                '}';
    }
}
