package com.xiyoulinux.hive.bloomfilter;

/**
 * Created by Junco on 14-10-29.
 */
import java.util.BitSet;

public class hiveBloomFilter {

    private BitSet BS_direct;
    //private BitSet BS_length;

    public hiveBloomFilter() {
        this.BS_direct = new BitSet(Integer.MAX_VALUE);
        //	this.BS_length = new BitSet(Integer.MAX_VALUE);
    }

    public int getHashCode( String url ) {
        return Math.abs( url.hashCode() % Integer.MAX_VALUE );
    }

    public int getHashCode( int num ) {
        String tmp = Integer.toString(num);
        return getHashCode(tmp);
    }

    public boolean judgeDirect( String url ) {
        int pos1 = getHashCode(url);
        int pos2 = getHashCode(pos1);
        int pos3 = getHashCode(pos2);
        return setBit(BS_direct, pos1, pos2, pos3);
    }

//	public boolean judgeLength( int length, String url ) {
//		int pos1 = getHashCode( length + url.hashCode() );
//		int pos2 = getHashCode(pos1);
//		int pos3 = getHashCode(pos2);
//		return setBit(BS_length, pos1, pos2, pos3);
//	}

    public synchronized boolean setBit( BitSet bitset, int pos1, int pos2, int pos3 ) {
        if( bitset.get(pos1) && bitset.get(pos2) && bitset.get(pos3) ) {
            //如果在当前位数组中三个标志位都已设置
            return true;
        }
        else {
            //若有至少一位未设置
            bitset.set(pos1);
            bitset.set(pos2);
            bitset.set(pos3);
            return false;
        }
    }

    public boolean exist( String url ) {
        //	int length = url.length();
        //	if( judgeDirect(url) && judgeLength(length, url) ) {
        if( judgeDirect(url) ) {
            //当前url已存在，为重复链接
            //System.out.println(url);
            return true;
        }
        else {
            //当前url不存在，不是重复链接
            return false;
        }
    }
}