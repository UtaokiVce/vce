package com.weilai9.dao.vo.api;

import com.weilai9.dao.entity.Category;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.EAN;

import java.util.List;

@Data
@ApiModel("商品分类VO")
public class CategoryVo {
    private Category category;
    private List<Category> categories;
}
