package com.weilai9.service.api;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Category;
import com.weilai9.dao.entity.Customer;
import com.weilai9.dao.vo.admin.AddCategoryVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * (User)表服务接口
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:20:47
 */
public interface ApiCategoryService extends IService<Category> {

    /**
     *  根据类型查询分类
     * @param categoryType
     * @return
     */
    public Result getCategoryByTypeList(Integer categoryType);

    /**
     *  分类管理
     * @param pageno
     * @param pagesize
     * @param cateType
     * @return
     */
    public Result getCategoryList(Integer pageno,Integer pagesize,Integer cateType,String name);

    /**
     *  根据父id获取分类列表
     * @param categoryId
     * @return
     */
    public Result getCategoryListByParentId(Integer categoryId);

    /**
     *  分类禁用/启用
     * @param categoryId
     * @param state
     * @return
     */
    public Result doCategoryEnable(Integer categoryId,Integer state);

    /**
     *  新增/编辑分类
     * @param addCategoryVO
     * @return
     */
    public Result saveCategory(AddCategoryVO addCategoryVO);

}