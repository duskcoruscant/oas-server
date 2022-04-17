package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwenbin.server.controller.managecenter.req.GetPositionListReq;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.core.web.response.SortingField;
import com.hwenbin.server.dto.PositionDTO;
import com.hwenbin.server.entity.Position;
import com.hwenbin.server.mapper.PositionMapper;
import com.hwenbin.server.service.PositionService;
import com.hwenbin.server.util.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @create 2022-03-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {

    @Resource
    private PositionMapper positionMapper;

    @Override
    public PageResult<Position> pageQuery(GetPositionListReq req) {
        return positionMapper.selectPage(req, Collections.singletonList(new SortingField("sort", SortingField.ORDER_ASC)),
                new MyLambdaQueryWrapper<Position>()
                        .likeIfPresent(Position::getCode, req.getCode())
                        .likeIfPresent(Position::getName, req.getName())
                        .eqIfPresent(Position::getStatus, req.getStatus())
        );
    }

    @Override
    public void add(PositionDTO positionDTO) {
        checkAddOrUpdate(null, positionDTO.getName(), positionDTO.getCode());
        final Position position = new Position();
        BeanUtil.copyProperties(positionDTO, position);
        positionMapper.insert(position);
    }

    @Override
    public void update(PositionDTO positionDTO) {
        // 校验
        checkAddOrUpdate(positionDTO.getId(), positionDTO.getName(), positionDTO.getCode());
        final Position position = new Position();
        BeanUtil.copyProperties(positionDTO, position);
        // TODO : 若状态发生变更，需考虑当前职位存在员工关联的情况处理
        positionMapper.updateById(position);
    }

    @Override
    public Position getDetail(Long id) {
        checkPositionExists(id);
        return positionMapper.selectById(id);
    }

    @Override
    public void delete(Long id) {
        checkPositionExists(id);
        // TODO : 若当前职位存在员工关联的情况处理
        positionMapper.deleteById(id);
    }

    @Override
    public Map<Long, String> getPositionMapByIds(Collection<Long> positionIds) {
        if (CollUtil.isEmpty(positionIds)) {
            return MapUtil.empty();
        }
        List<Position> positions = positionMapper.selectBatchIds(positionIds);
        return positions.stream().collect(Collectors.toMap(Position::getId, Position::getName));
    }

    private void checkAddOrUpdate(Long id, String name, String code) {
        // 校验自己存在
        checkPositionExists(id);
        // 校验职位名的唯一性
        checkPositionNameUnique(id, name);
        // 校验职位编码的唯一性
        checkPositionCodeUnique(id, code);
    }

    private void checkPositionExists(Long id) {
        if (id == null) {
            return;
        }
        AssertUtils.asserts(positionMapper.selectById(id) != null, ResultCode.POSITION_NOT_FOUND);
    }

    private void checkPositionNameUnique(Long id, String name) {
        Position position = positionMapper.selectOne("name", name);
        if (position == null) {
            return;
        }
        AssertUtils.asserts(id != null, ResultCode.POSITION_NAME_DUPLICATE);
        AssertUtils.asserts(position.getId().equals(id), ResultCode.POSITION_NAME_DUPLICATE);
    }

    private void checkPositionCodeUnique(Long id, String code) {
        Position position = positionMapper.selectOne("code", code);
        if (position == null) {
            return;
        }
        AssertUtils.asserts(id != null, ResultCode.POSITION_CODE_DUPLICATE);
        AssertUtils.asserts(position.getId().equals(id), ResultCode.POSITION_CODE_DUPLICATE);
    }

}
