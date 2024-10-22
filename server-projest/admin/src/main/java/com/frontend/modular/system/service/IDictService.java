package com.frontend.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.frontend.modular.system.model.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 字典服务
 */
public interface IDictService extends IService<Dict> {

    /**
     * 添加字典
     */
    void addDict(String dictName, String dictValues);

    /**
     * 编辑字典
     */
    void editDict(Integer dictId, String dictName, String dicts);

    /**
     * 删除字典
     */
    void delteDict(Integer dictId);

    /**
     * 根据编码获取词典列表
     */
    List<Dict> selectByCode(@Param("code") String code);

    /**
     * 查询字典列表
     */
    List<Map<String, Object>> list(@Param("condition") String conditiion);

}
