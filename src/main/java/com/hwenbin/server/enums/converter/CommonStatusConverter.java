package com.hwenbin.server.enums.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.hwenbin.server.core.exception.ServiceException;

import static com.hwenbin.server.enums.CommonStatusEnum.DISABLE;
import static com.hwenbin.server.enums.CommonStatusEnum.ENABLE;

/**
 * @author hwb
 * @date 2022/05/26 17:04
 */
public class CommonStatusConverter implements Converter<Integer> {

    /**
     * 将 Excel 展示的数据 转换为 数据库中存储的数据
     *
     * @param cellData 单元格值
     */
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                     GlobalConfiguration globalConfiguration) {
        if (ENABLE.getName().equals(cellData.getStringValue())) {
            return ENABLE.getStatus();
        } else if (DISABLE.getName().equals(cellData.getStringValue())) {
            return DISABLE.getStatus();
        } else {
            throw new ServiceException("状态字段值必须为开启或关闭");
        }
    }

    /**
     * 将从数据库中查到的数据转换为 Excel 展示的数据
     *
     * @param value 枚举值
     */
    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        if (ENABLE.getStatus().equals(value)) {
            return new WriteCellData<>(ENABLE.getName());
        } else if (DISABLE.getStatus().equals(value)) {
            return new WriteCellData<>(DISABLE.getName());
        } else {
            throw new ServiceException("状态字段必须为0（开启）或1（关闭）");
        }
    }

}
