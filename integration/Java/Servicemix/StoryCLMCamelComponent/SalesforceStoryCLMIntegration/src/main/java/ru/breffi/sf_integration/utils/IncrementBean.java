package ru.breffi.sf_integration.utils;

import java.util.List;

public class IncrementBean {

	public Integer Increment(Integer arg,Integer incValue){
		/*System.out.println("Increment arg+incvakue:"+arg+"+"+incValue);
		System.out.println(arg);
		System.out.println(incValue);*/
		//return (arg+incValue>2000)? 2000:arg+incValue;
		return arg+incValue;
	}
	public Integer LoopCount(Integer count, Integer limit){
		if (limit == 0) return 0;
		int del = count/limit;
		int mod = count%limit ==0 ?0:1;
		/*System.out.println("LoopCount");
		System.out.println( del + mod);*/
		return del + mod;
	}
	
	public int OneIfNotNull(Object one){
		return one==null?0:(one.toString().equals("")?0:1);
	}
	
	public int OneIfNotNull(){
		return 0;
	}
	
	public List<Object> Range(List<Object> list, int start, int count){
		return list.subList(start,start + count);
	}
	
	
	public int Decrement(int value){
		return value-1;
	}
	
	public String FirstNotNull(String arg1,String arg2){
		return (arg1!="")?arg1:arg2;
	}
}
