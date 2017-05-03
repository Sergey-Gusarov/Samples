# Camel StoryCLM component #

Данный компонент предоставляет конечные точки источника (producer endpoint) для взаимодействия с API StoryCLM

## URI format ##

    storyclm:command?options

Поддерживается следующий список команд

## Команды ##

* Find - возвращает записи таблицы. Опции:
  
  * tableId - идентификатор таблицы
  * entityType - тип возвращаемого объекта (задается полное имя типа)
  * maxByField (не реализовано) - возвращает запись с максимальным значением указанного поля.
  

* Query - поиск записей с помощью запроса. Опции:
  
  * tableId - идентификатор таблицы
  * query - запрос в формате [Tables Query](https://github.com/storyclm/documentation/blob/master/TABLES_QUERY.md)
* Upsert - Вставка или обновление данных. Телом сообщения должны быть объекты для вставки/обновления. В зависимости от наличия идентификатора в объекте, он либо вставится, либо обновится. Опции:
  
  * tableId - идентификатор таблицы
  * storyIdName - название столбца идентификатора таблицы (обычно _id)

Все опции поддерживают simple expression. Если для опции найден одноименный заголовок (header) сообщения, его значение становится значением опции.
Пример конечно точки возвращающей все записи таблицы

    <to uri="storyCLM:Find?tableId=${header.getLogTableId}&amp;entityType=ru.breffi.sf_integration.dto.StoryLog&amp;maxByField=Date"/>

Для использования необходимо клонировать git репозиторий и установить в локальный maven репозиторий с помощью команды  mvn install. Для добавления в проект в pom файле укажите 

     <dependency>
    	<groupId>ru.breffi</groupId>
    	<artifactId>storyclm-camel-component</artifactId>
    	<version>0.0.1-SNAPSHOT</version>
    	    <scope>compile</scope>
    </dependency>



  
