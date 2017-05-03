package ru.breffi.sf_integration.utils;

import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;


/**
 * Извлекает из пришедших объектов (story) Id и подставляет в сущности из SF
 * @author tselo
 *
 */
public class CheckExistingAgregationStrategy implements AggregationStrategy{
	@SuppressWarnings("unchecked")
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		List<IStoryEntity> sfs =(List<IStoryEntity>) oldExchange.getIn().getBody();
		List<IStoryEntity> storys = (List<IStoryEntity>) newExchange.getIn().getBody();
		for(IStoryEntity story :storys)
			for(IStoryEntity sf :sfs)
				if (sf.getSalesForceId().equals(story.getSalesForceId()))
				{
					sf.setStoryId(story.getStoryId());
					break;
				}
		return oldExchange;
	}
	

}
