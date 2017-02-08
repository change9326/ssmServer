package com.ssm.controller;


import com.ssm.po.ItemsCustom;
import com.ssm.service.ItemsService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Api(value = "/items", description = "Operations about user")
@Controller
//为了对url进行分类管理 ，可以在这里定义根路径，最终访问url是根路径+子路径
//比如：商品列表：/items/queryItems.action
@RequestMapping(value = "/items")
public class ItemsController {

    @Autowired
    private ItemsService itemsService;

    // 商品查询
    @ApiOperation(value = "根据用户id查询用户信息", httpMethod = "GET", produces = "application/json")
    @RequestMapping("/queryItems")
    public ModelAndView queryItems() throws Exception {
        // 调用service查找 数据库，查询商品列表
        List<ItemsCustom> itemsList = itemsService.findItemsList(null);
        // 返回ModelAndView
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("itemsList", itemsList);
        modelAndView.setViewName("items/itemsList");
        return modelAndView;
    }

    @RequestMapping(value = "/editItems", method = {RequestMethod.POST, RequestMethod.GET})
    //@RequestParam里边指定request传入参数名称和形参进行绑定。
    //通过required属性指定参数是否必须要传入
    //通过defaultValue可以设置默认值，如果id参数没有传入，将默认值和形参绑定。
    public String editItems(Model model, @RequestParam(value = "id", required = true) Integer items_id) throws Exception {

        //调用service根据商品id查询商品信息
        ItemsCustom itemsCustom = itemsService.findItemsById(items_id);

        //通过形参中的model将model数据传到页面
        //相当于modelAndView.addObject方法
        model.addAttribute("itemsCustom", itemsCustom);

        return "items/editItems";
    }

    //商品信息修改提交
    // 在需要校验的pojo前边添加@Validated，在需要校验的pojo后边添加BindingResult
    // bindingResult接收校验出错信息
    // 注意：@Validated和BindingResult bindingResult是配对出现，并且形参顺序是固定的（一前一后）。
    // value={ValidGroup1.class}指定使用ValidGroup1分组的 校验
    @RequestMapping("/editItemsSubmit")
    public String editItemsSubmit(
            Model model,
            HttpServletRequest request, Integer id,
            @ModelAttribute("items")
            @Validated ItemsCustom itemsCustom,
            BindingResult bindingResult,
            MultipartFile items_pic) throws Exception {
        // 获取校验错误信息
        if (bindingResult.hasErrors()) {
            // 输出错误信息
            List<ObjectError> allErrors = bindingResult.getAllErrors();

            for (ObjectError objectError : allErrors) {
                // 输出错误信息
                System.out.println(objectError.getDefaultMessage());

            }
            // 将错误信息传到页面
            model.addAttribute("allErrors", allErrors);


            //可以直接使用model将提交pojo回显到页面
            model.addAttribute("items", itemsCustom);

            // 出错重新到商品修改页面
            return "items/editItems";
        }

        //原始名称
        String originalFilename = items_pic.getOriginalFilename();
        //上传图片
        if (items_pic != null && originalFilename != null && originalFilename.length() > 0) {

            //存储图片的物理路径
            String pic_path1 = "F:\\develop\\upload\\temp\\";
            String pic_path = "D:\\Work\\2017\\DevSoft\\Java\\tomcat\\upload\\temp\\";


            //新的图片名称
            String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            //新图片
            File newFile = new File(pic_path + newFileName);

            //将内存中的数据写入磁盘
            items_pic.transferTo(newFile);

            //将新图片名称写到itemsCustom中
            itemsCustom.setPic(newFileName);


        }

        //调用service更新商品信息，页面需要将商品信息传到此方法
        itemsService.updateItems(id, itemsCustom);

        //重定向到商品查询列表
        //return "redirect:queryItems.action";
        //页面转发
        //return "forward:queryItems.action";
        return "success";
    }


    /**
     * Json 数据交互
     */
    @RequestMapping("/itemsView/{id}")
    public
    @ResponseBody
    ItemsCustom itemsView(@PathVariable("id") Integer id) throws Exception {

        //调用service查询商品信息
        ItemsCustom itemsCustom = itemsService.findItemsById(id);

        return itemsCustom;

    }

    //查询商品信息，输出json
    ///itemsView/{id}里边的{id}表示占位符，通过@PathVariable获取占位符中的参数，
    //如果占位符中的名称和形参名一致，在@PathVariable可以不指定名称
    @RequestMapping("/itemsView1/{id}")
    public
    @ResponseBody
    ItemsCustom itemsView1(@PathVariable("id") Integer id) throws Exception {

        //调用service查询商品信息
        ItemsCustom itemsCustom = itemsService.findItemsById(id);

        return itemsCustom;

    }
}

