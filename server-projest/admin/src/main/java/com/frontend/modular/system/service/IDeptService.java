package com.frontend.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.frontend.core.node.ZTreeNode;
import com.frontend.modular.system.model.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门服务
 *
 * @author fengshuonan
 * @date 2017-04-27 17:00
 */
public interface IDeptService extends IService<Dept> {

    /**
     * 删除部门
     */
    void deleteDept(Integer deptId);

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 获取所有部门列表
     */
    List<Map<String, Object>> list(@Param("condition") String condition);
}
