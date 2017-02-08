package com.ssm.mapper;


import com.ssm.po.ItemsCustom;
import com.ssm.po.ItemsQueryVo;

import java.util.List;

public interface ItemsMapperCustom {
    //商品查询列表
    public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception;
}