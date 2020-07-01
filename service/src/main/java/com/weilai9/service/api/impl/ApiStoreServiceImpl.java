package com.weilai9.service.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Category;
import com.weilai9.dao.entity.Goods;
import com.weilai9.dao.entity.Store;
import com.weilai9.dao.mapper.CategoryMapper;
import com.weilai9.dao.mapper.StoreMapper;
import com.weilai9.service.api.ApiCategoryService;
import com.weilai9.service.api.ApiStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * (User)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:20:48
 */
@Slf4j
@Service("apiStoreService")
public class ApiStoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements ApiStoreService {

    @Resource
    private StoreMapper storeMapper;

    @Override
    public Result getCommendStoreList(){
        List<Map<String,Object>> storeList=storeMapper.getCommendStoreList(4);
        if(storeList==null){
            storeList=new ArrayList<>();
        }
        return new Result(storeList);
    }

    @Override
    public Result adminGetCommendStoreList() {
        List<Store> stores = this.storeMapper.selectList(new QueryWrapper<Store>().eq("isRem", 1).eq("state", 1).orderByDesc("remNum"));
        if (stores==null){
            return Result.Error(201,"无门店");
        }
        return new Result(stores);
    }

    @Override
    public Result adminCommendStoreSort(Integer id, Integer operation) {
        //当前商店
        Store store = this.storeMapper.selectById(id);
        //当前排序
        Integer currSort = store.getRemNum();
        //上移 当前sort+1  将上一条数据sort-1
        if(operation == 1){
            if(currSort == 4){
                return Result.Error("操作失败，已经是第一位了！");
            }
            store.setRemNum(store.getRemNum()+1);
            Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().eq("remNum", currSort + 1));

            if(store1 != null){
                store1.setRemNum(currSort);
                this.storeMapper.updateById(store1);
            }
            this.storeMapper.updateById(store);
            return Result.OK("操作成功！");

        }else if(operation == 2){
            //下移 当前sort-1  将下一条数据sort+1
            if(currSort == 1){
                return Result.Error("操作失败，已经是最后一位了！");
            }
            store.setRemNum(store.getRemNum()-1);
            Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().eq("remNum", currSort - 1));

            if(store1 != null){
                store1.setRemNum(currSort);
                this.storeMapper.updateById(store1);
            }
            this.storeMapper.updateById(store);
            return Result.OK("操作成功！");
        }else if(operation == 3){
            //置顶 当前sort设为4，比他高的-1
            List<Store> storeList1 = this.storeMapper.selectList(new QueryWrapper<Store>().eq("isRem", 1));
            Integer size = storeList1.size();
            if(currSort == size){
                return Result.Error("置顶失败，已经是第一位了！");
            }
            store.setRemNum(size);
            List<Store> storeList= list(new QueryWrapper<Store>().gt("remNum", currSort));
            if(storeList.size()>0){
                storeList.forEach(goods2->{
                    goods2.setRemNum(goods2.getRemNum()-1);
                    this.storeMapper.updateById(goods2);
                });
            }
            this.storeMapper.updateById(store);
            return Result.OK("操作成功！");
        }

        return Result.Error("操作失败");
    }

    @Override
    public Result store2NotRem(Integer storeId) {
        if (storeId==null){
            return Result.Error(201,"参数有误");
        }
        Store store = this.storeMapper.selectById(storeId);



        List<Store> goodsList = this.storeMapper.selectList(new QueryWrapper<Store>().ge("remNum", store.getRemNum()));
        for (Store g : goodsList) {
            g.setRemNum(g.getRemNum()-1);
            this.storeMapper.updateById(g);
        }
        store.setIsRem(0);
        store.setRemNum(0);
        this.storeMapper.updateById(store);
        return Result.OK();
    }
}