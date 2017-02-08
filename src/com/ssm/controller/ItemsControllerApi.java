package com.ssm.controller;

import com.ssm.exception.CustomException;
import com.ssm.po.ItemsCustom;
import com.ssm.service.ItemsService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Mryu93
 * @version 1.0
 * @date 2017/2/8
 * @company
 * @desc:
 */
@Controller
@RequestMapping(value = "/itemsApi")
@Api(description = "商品列表Api")
public class ItemsControllerApi {
    @Autowired
    private ItemsService itemsService;

    /**
     * 商品查询
     */
    @RequestMapping(value = "/queryItems", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "商品查询", httpMethod = "GET", produces = "application/json")
    @ResponseBody
    public List<ItemsCustom> queryItems(ItemsCustom itemsCustom) throws Exception {
        List<ItemsCustom> itemsList = itemsService.findItemsList(null);
        return itemsList;
    }

    @RequestMapping("/queryItemsById")
    @ApiOperation(value = "根据商品ID查询", httpMethod = "GET", produces = "application/json")
    @ResponseBody
    public ItemsCustom queryItemsById(Model model, @RequestParam(value = "id", required = true) Integer id) throws Exception {

        //调用service查询商品信息
        ItemsCustom itemsCustom = itemsService.findItemsById(id);
        if (itemsCustom == null) {
            throw new CustomException("商品信息不存在!");
        }
        return itemsCustom;
    }

}
