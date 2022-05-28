package com.hwenbin.server.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.hwenbin.server.enums.converter.CommonStatusConverter;
import com.hwenbin.server.enums.converter.SexConverter;
import lombok.Data;

import java.util.Date;

/**
 * @author hwb
 * @date 2022/05/26 12:16
 */
@Data
public class EmployeeExportExcelDTO {

    /**
     * 员工id
     */
    @ExcelProperty(index = 0, value = "员工id")
    private Long id;

    /**
     * 员工姓名
     */
    @ExcelProperty(index = 1, value = "姓名")
    private String name;

    /**
     * 账户昵称
     */
    @ExcelProperty(index = 2, value = "账户昵称")
    private String nickname;

    /**
     * 员工性别 1女，2男
     */
    @ExcelProperty(index = 3, value = "性别", converter = SexConverter.class)
    private Integer sex;

    /**
     * 员工电话
     */
    @ExcelProperty(index = 4, value = "电话")
    private String phone;

    /**
     * 员工邮箱
     */
    @ExcelProperty(index = 5, value = "邮箱")
    private String email;

    /**
     * 员工生日
     */
    @ExcelProperty(index = 6, value = "生日")
    @DateTimeFormat(value = "yyyy年MM月dd日")
    @ColumnWidth(value = 15)
    private Date birthday;

    /**
     * 员工职位
     */
    @ExcelProperty(index = 7, value = "职位")
    private String positions;

    /**
     * 部门名称
     */
    @ExcelProperty(index = 8, value = "部门")
    private String deptName;

    /**
     * 入职日期
     */
    @ExcelProperty(index = 9, value = "入职日期")
    @DateTimeFormat(value = "yyyy年MM月dd日")
    @ColumnWidth(value = 15)
    private Date entryDate;

    /**
     * 备注
     */
    @ExcelProperty(index = 10, value = "备注")
    private String remark;

    /**
     * 员工状态
     */
    @ExcelProperty(index = 11, value = "状态", converter = CommonStatusConverter.class)
    private Integer status;

}
