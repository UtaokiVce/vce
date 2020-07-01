package com.weilai9.service.api.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.auth.JwtTokenUtil;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.*;
import com.weilai9.common.utils.oss.OSSUtil;
import com.weilai9.common.utils.redis.RedisUtil;
import com.weilai9.dao.entity.Category;
import com.weilai9.dao.entity.Customer;
import com.weilai9.dao.entity.CustomerRole;
import com.weilai9.dao.mapper.CategoryMapper;
import com.weilai9.dao.mapper.CustomerMapper;
import com.weilai9.dao.vo.admin.AddCategoryVO;
import com.weilai9.service.api.ApiCategoryService;
import com.weilai9.service.api.ApiCustomerService;
import com.weilai9.service.base.CustomerRoleService;
import com.weilai9.service.base.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;

/**
 * (User)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:20:48
 */
@Slf4j
@Service("apiCategoryService")
public class ApiCategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ApiCategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Result getCategoryByTypeList(Integer categoryType){

        List<Category> categoryOne=categoryMapper.selectList(new QueryWrapper<Category>().eq("cateType",categoryType).eq("`enable`",1).eq("parentId",0).orderByDesc("orderIndex"));
        List<Category> categoryTwo=categoryMapper.selectList(new QueryWrapper<Category>().eq("cateType",categoryType).eq("`enable`",1).ne("parentId",0).orderByDesc("orderIndex"));

        List<Map<String,Object>> resList=new ArrayList<Map<String, Object>>();
        if(categoryOne!=null && categoryOne.size()>0){
            for(Category c:categoryOne){
                Map<String,Object> map=new HashMap<String, Object>();
                List<Category> dataList=new ArrayList<Category>();
                if(categoryTwo!=null && categoryTwo.size()>0){
                    for(Category c2:categoryTwo){
                        if(c.getCategoryId().intValue()==c2.getParentId().intValue()){
                            dataList.add(c2);
                        }
                    }
                }
                map.put("categoryId",c.getCategoryId());
                map.put("name",c.getName());
                map.put("dataList",dataList);
                resList.add(map);
            }
        }
        return new Result(resList);
    }

    @Override
    public Result getCategoryList(Integer pageno,Integer pagesize,Integer cateType,String name){
        IPage page = new Page(null == pageno ? 1 : pageno, null == pagesize ? 10 : pagesize);
        QueryWrapper<Category> queryWrapper=new QueryWrapper<Category>();
        if(cateType!=null){
            queryWrapper.eq("cateType",cateType);
        }
        if(StringUtils.isNotEmpty(name)){
            queryWrapper.like("name",name);
        }
        IPage<Category> iPage = categoryMapper.selectPage(page,queryWrapper);
        return new Result(iPage);
    }

    @Override
    public Result getCategoryListByParentId(Integer categoryId){
        List<Category> dataList=categoryMapper.selectList(new QueryWrapper<Category>().eq("categoryId",categoryId).eq("`enable`",1).orderByDesc("orderIndex"));
        if(dataList==null){
            dataList=new ArrayList<>();
        }
        return new Result(dataList);
    }

    @Override
    public Result doCategoryEnable(Integer categoryId,Integer state){
        int count=categoryMapper.update(null,new UpdateWrapper<Category>().set("`enable`",state).eq("categoryId",categoryId));
        if(count<=0){
            return Result.Error("失败");
        }
        return Result.OK("成功");
    }

    @Override
    public Result saveCategory(AddCategoryVO addCategoryVO){
        Category category=new Category();
        category.setCateType(addCategoryVO.getCateType());
        category.setParentId(addCategoryVO.getParentId());
        category.setImg(addCategoryVO.getImg());
        category.setName(addCategoryVO.getName());
        category.setOrderIndex(addCategoryVO.getOrderIndex());
        int count=0;
        if(addCategoryVO.getCategoryId()!=null){
            count=categoryMapper.update(category,new UpdateWrapper<Category>().eq("categoryId",addCategoryVO.getCategoryId()));
        }else{
            count=categoryMapper.insert(category);
        }
        if(count<=0){
            return Result.Error("失败");
        }
        return Result.OK("成功");
    }
}