package ru.breffi.story2sf.services;

/**
 * ≈сли названи€ идентификационных полей, как здесь, можно использовать данный класс как базовый
 * @author tselo
 *
 */
public class StandartStoryEntity implements IStoryEntity {
	public String _id;
	public String SFId;
	
	public void setSalesForceId(String id) {
		SFId = id;
	}
	
	public String getStoryId() {
		return this._id;
	}
}
