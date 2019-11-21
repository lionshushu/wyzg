package com.wyzg.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyzg.enums.ExceptionEnums;
import com.wyzg.exception.WyzgException;
import com.wyzg.mapper.CarSourceMapper;
import com.wyzg.pojo.CarSource;
import com.wyzg.pojo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarSourceService {

    @Autowired
    private CarSourceMapper carSourceMapper;

    /**
     * 查询所有车源并分页
     * @return
     */
    public PageResult<CarSource> queryCarByPage(Integer page, Integer rows, String sortBy,Boolean desc,String key) {
        //分页
        PageHelper.startPage(page,rows);
        //创建查询条件
        Example example = new Example(CarSource.class);
        // 过滤
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key);
        }
        //排序
        if(StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc ? "DESC" : "ASC");
            example.setOrderByClause(orderByClause);
        }
        List<CarSource> sources = carSourceMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(sources)){
            throw new WyzgException(ExceptionEnums.NO_CARSOURCE_COULD_USE);
        }
        //封装分页对象
        PageInfo<CarSource> info = new PageInfo<>(sources);

        PageResult<CarSource> result = new PageResult<>();
        result.setItems(sources);
        result.setTotal(info.getTotal());
        result.setTotalPage(Long.valueOf(info.getPages()));
        return result;
    }

    /**
     * 根据车源id查询车源详情
     * @param id
     */
    public CarSource queryCarById(Integer id) {
        CarSource carSource = carSourceMapper.selectByPrimaryKey(id);
        return carSource;
    }

    /**
     * 添加车源信息
     * @param carSource
     * @return
     */
    public void addCar(CarSource carSource) {
        carSourceMapper.insertSelective(carSource);
    }

    /**
     * 修改车源信息
     * @param carSource
     * @return
     */
    public void updateCar(CarSource carSource) {

        carSourceMapper.updateByPrimaryKeySelective(carSource);
    }

    /**
     * 通过车源id删除车源信息
     * @param id
     * @return
     */
    public void deleteCarById(Integer id) {
        carSourceMapper.deleteByPrimaryKey(id);
    }

}
