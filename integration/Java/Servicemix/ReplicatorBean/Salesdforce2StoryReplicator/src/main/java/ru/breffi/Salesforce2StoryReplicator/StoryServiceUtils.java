package ru.breffi.Salesforce2StoryReplicator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import ru.breffi.EntityTypeConverterServicePackage.IStoryEntity;
import ru.breffi.storyclmsdk.StoryCLMTableService;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;


public class StoryServiceUtils {
	public static List<IStoryEntity> GetExisting(String fieldName, List<String> fieldValues, StoryCLMTableService<IStoryEntity> storyService) throws AuthFaliException, AsyncResultException
	{
		String valuesStr = "";
		int maxStrSize = 100;
		List<IStoryEntity> result = new ArrayList<IStoryEntity>();
		for (int i = 0; i < fieldValues.size(); i++) {
			// tempStoryObjects.add(converterService.ConvertToStory(sObjects[i]));
			valuesStr += ",\"" + fieldValues.get(i) + "\"";
			if ((valuesStr.length() > maxStrSize) || (i + 1 == fieldValues.size())) {
				valuesStr = valuesStr.substring(1, valuesStr.length());
				String storyQuery = MessageFormat.format("[{0}][in][{1}]", fieldName, valuesStr);
				List<IStoryEntity> entities = storyService.FindAllSync(storyQuery);
				result.addAll(entities);
				valuesStr = "";
			}
		}
		return result;
	}
}
