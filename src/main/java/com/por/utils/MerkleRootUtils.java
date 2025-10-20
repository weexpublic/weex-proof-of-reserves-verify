package com.por.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 默克尔树工具类
 * @author Falan
 * @date 2025-10-11 19:27:36
 */
public class MerkleRootUtils {

    /**
     * 获取默克尔根
     * @param hashList      hash列表
     * @return              默克尔根
     */
    public static String getMerkleRoot(List<String> hashList){

        //不能为空
        if (null == hashList || hashList.isEmpty()){
            throw new IllegalStateException("origin hash list can't be empty");
        }

        //如果列表里面只有一个元素，那么就是默克尔根
        if (1 == hashList.size()){
            return hashList.get(0);
        }

        //如果列表里面元素个数不是2的倍数，那么需要将最后一个元素添加到列表尾部
        if (0 != hashList.size() % 2){
            hashList.add(hashList.get(hashList.size() - 1));
        }

        List<String> newHashList = new ArrayList<>();

        //两两连接，计算hash
        for (int i = 0; i < hashList.size(); i += 2){
            newHashList.add(HashUtils.calcHash(hashList.get(i) + hashList.get(i + 1)));
        }

        //递归调用
        return getMerkleRoot(newHashList);
    }
}
