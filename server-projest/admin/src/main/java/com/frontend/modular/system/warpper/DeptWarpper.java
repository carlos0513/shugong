package com.frontend.modular.system.warpper;

import com.frontend.core.common.constant.factory.ConstantFactory;
import com.frontend.core.base.warpper.BaseControllerWarpper;
import com.frontend.core.util.ToolUtil;

import java.util.Map;

/**
 * 部门列表的包装
 */
public class DeptWarpper extends BaseControllerWarpper {

    public DeptWarpper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {

        Integer pid = (Integer) map.get("pid");

        if (ToolUtil.isEmpty(pid) || pid.equals(0)) {
            map.put("pName", "--");
        } else {
            map.put("pName", ConstantFactory.me().getDeptName(pid));
        }
    }

}
