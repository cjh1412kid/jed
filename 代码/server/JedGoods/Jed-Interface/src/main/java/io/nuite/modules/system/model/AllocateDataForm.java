package io.nuite.modules.system.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AllocateDataForm {

	 private Integer regionSeq;
	 
	 private List<AllocateForm> allocateList;
	 
	 /**
	     * map转化为List<ToDataBo>
	     * @param map
	     * @return
	     */
	    public static List<AllocateDataForm> createByMap(Map<Integer, List<AllocateForm>> map){
	        return map.entrySet().stream().map(AllocateDataForm::of).collect(Collectors.toList());
	    }

	    /**
	     * 一个Map.Entry<String, List<AllocateForm>>对应转化为一个ToDataBo
	     * @param entry
	     * @return
	     */
	    public static AllocateDataForm of(Map.Entry<Integer, List<AllocateForm>> entry){
	    	AllocateDataForm dataBo = new AllocateDataForm();
	        dataBo.setRegionSeq(entry.getKey());
	        dataBo.setAllocateList(entry.getValue().stream().collect(Collectors.toList()));
	        return dataBo;
	    }
	    
	    @Getter
	    @Setter
	    static class ToDataIllnessBo{
	        private String name;
	        private List<ToDataQuotaBo> quotaList;

	        /**
	         * 一个Map.Entry<String, List<String>>对应转化为一个ToDataIllnessBo
	         * @param entry
	         * @return
	         */
	        public static ToDataIllnessBo of(Map.Entry<String, List<String>> entry){
	            ToDataIllnessBo dataIllnessBo = new ToDataIllnessBo();
	            dataIllnessBo.setName(entry.getKey());
	            dataIllnessBo.setQuotaList(entry.getValue().stream().map(ToDataQuotaBo::new).collect(Collectors.toList()));
	            return dataIllnessBo;
	        }
	    }

	    @Getter
	    @Setter
	    @AllArgsConstructor
	    static class ToDataQuotaBo {
	        private String name;
	    }
}
