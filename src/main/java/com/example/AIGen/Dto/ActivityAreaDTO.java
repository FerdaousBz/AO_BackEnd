package com.example.AIGen.Dto;

import java.util.List;

public class ActivityAreaDTO {

	 private String id;
	    private List<ActivityOption> option;
	    
	    public static class ActivityOption {
	        private String id;
	        private String value;
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
	        
	    }

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public List<ActivityOption> getOption() {
			return option;
		}

		public void setOption(List<ActivityOption> option) {
			this.option = option;
		}
	    
	    
}
