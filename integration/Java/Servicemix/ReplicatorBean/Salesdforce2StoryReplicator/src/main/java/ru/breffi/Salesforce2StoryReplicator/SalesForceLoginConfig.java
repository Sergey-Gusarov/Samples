package ru.breffi.Salesforce2StoryReplicator;

public class SalesForceLoginConfig {
	 public String UserName;
	 public String Password;
	 public String AuthEndpoint;
	 
		public void setAuthEndpoint(String AuthEndpoint){
			this.AuthEndpoint = AuthEndpoint;
		}
	 
		public void setUserName(String userName){
			this.UserName = userName;
		}
		public void setPassword(String password){
			this.Password = password;
		}
}
