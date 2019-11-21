package com.wyzg.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyzg.enums.ExceptionEnums;
import com.wyzg.exception.WyzgException;
import com.wyzg.mapper.GoodsSourceMapper;
import com.wyzg.pojo.CarSource;
import com.wyzg.pojo.GoodsSource;
import com.wyzg.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class GoodsSourceService {

    @Autowired
    private GoodsSourceMapper goodsSourceMapper;

    /**
     * 查询货源信息并完成分页
     * @param page
     * @param rows
     * @param sortBy
     * @return
     */
    public PageResult<GoodsSource> queryGoodsByPage(Integer page, Integer rows, String sortBy) {

        //分页
        PageHelper.startPage(page,rows);
        //创建查询条件
        Example example = new Example(GoodsSource.class);
        //默认排序
        example.setOrderByClause("create_time");
        List<GoodsSource> sources = goodsSourceMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(sources)){
            throw new WyzgException(ExceptionEnums.NO_CARSOURCE_COULD_USE);
        }
        //封装分页对象
        PageInfo<GoodsSource> info = new PageInfo<>(sources);

        PageResult<GoodsSource> result = new PageResult<>();
        result.setItems(sources);
        result.setTotal(info.getTotal());
        result.setTotalPage(Long.valueOf(info.getPages()));
        return result;

    }

    /**
     * 添加货源信息
     * @param goodsSource
     */
    public void insertGoods(GoodsSource goodsSource) {

        goodsSourceMapper.insertSelective(goodsSource);
    }


    /**
     * 通过货源id查询货源详情
     */
    public GoodsSource queryGoodsById(Integer id) {

        GoodsSource goodsSource = goodsSourceMapper.selectByPrimaryKey(id);

        return goodsSource;
    }

    /**
     * 更新货源信息
     * @param goodsSource
     */
    public void updateGoods(GoodsSource goodsSource) {

        goodsSourceMapper.updateByPrimaryKeySelective(goodsSource);
}

    /**
     * 根据id删除货源信息
     * @param id
     */
    public void deleteGoodsById(Integer id) {
        goodsSourceMapper.deleteByPrimaryKey(id);
    }

}
