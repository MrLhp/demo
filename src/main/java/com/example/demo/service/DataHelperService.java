package com.example.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DataHelperService {

    public <T> void store(final String dataPrefix, final Date date, final T data);

    /**
     * 创建非日期一次性文件
     *
     * @param dataPrefix 文件名
     * @param data       数据
     * @return
     */
    public <T> void store(final String dataPrefix, final List<T> data);

    /**
     * 创建非日期一次性文件(数组)
     *
     * @param dataPrefix 文件名
     * @param data       数据
     * @param catalogs   条件
     */
    public <T> void store(final String dataPrefix, final T data, String... catalogs);

    public <T> void store(final String dataPrefix, final Date date, String cityCode, final T data);

    public <T> void storeOne(final String dataPrefix, String filmCode, final T data);

    /**
     * 在dataPrefix文件夹下生成“filmCode.json”文件
     *
     * @param dataPrefix
     * @param filmCode
     * @param data
     */
    public <T> void storeOneExt(final String dataPrefix, String filmCode, final T data);

    public <T> void storeExt(final String dataPrefix, final T data, final Date date, String... catalogs);

    public <T> void storeExtList(final String dataPrefix, final List<T> data, final Date date, String... catalogs);

    /**
     * 创建非日期一次性文件
     *
     * @param <K>
     * @param dataPrefix 文件名
     * @param data       数据
     * @return
     */
    public <K, V> void storeMap(final String dataPrefix, final Map<K, V> data);

    /**
     * @param dataPrefix
     * @param data
     * @param date
     * @param isDirDate  文件夹生成过程中是否使用日期分级
     * @param catalogs
     */
    public <T> void storeExtList(final String dataPrefix, final List<T> data, final Date date, boolean isDirDate,
                                 String... catalogs);

    public <T> T load(final String dataPrefix, final Date date, Class<T> clazz);

    /**
     * 查询非日期一次性文件
     *
     * @param dataPrefix 文件名
     * @param clazz      文件保存对象
     * @return
     */
    public <T> List<T> load(final String dataPrefix, Class<T> clazz);

    public <T> List<T> loadListFromDir(String dataPrefix, Class<T> clazz);

    public <T> T load(final String dataPrefix, Class<T> clazz, String... catalogs);

    public <T> T load(final String dataPrefix, final Date date, String cityCode, Class<T> clazz);

    public <T> T loadExt(final String dataPrefix, Class<T> clazz, final Date date, String... catalogs);

    public <T> List<T> loadExtList(final String dataPrefix, Class<T> clazz, final Date date, String... catalogs);

    public <T> List<T> loadExtList(final String dataPrefix, Class<T> clazz, final Date date, boolean isDirDate,
                                   String... catalogs);

    public <T> T loadOne(final String dataPrefix, String filmCode, Class<T> clazz);

    /**
     * 在dataPrefix文件夹下生成“filmCode.json”文件
     *
     * @param dataPrefix
     * @param filmCode
     * @param data
     */
    public <T> T loadOneExt(final String dataPrefix, String filmCode, Class<T> clazz);

    /**
     * 查询非日期一次性文件
     *
     * @param dataPrefix 文件名
     * @param clazz      文件保存对象
     * @return
     */
    public <K, V> Map<K, V> loadMap(final String dataPrefix, TypeReference<Map<K, V>> MAP_TYPE);


    public String writeStringToFile(File file, String data, String encoding) throws IOException;

    public String readFileToString(File file, String encoding) throws IOException;


}