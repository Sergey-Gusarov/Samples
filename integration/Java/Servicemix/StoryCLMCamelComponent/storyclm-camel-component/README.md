# Camel StoryCLM component #

������ ��������� ������������� �������� ����� ��������� (producer endpoint) ��� �������������� � API StoryCLM

## URI format ##

    storyclm:command?options

�������������� ��������� ������ ������

## ������� ##

* Find - ���������� ������ �������. �����:
  
  * tableId - ������������� �������
  * entityType - ��� ������������� ������� (�������� ������ ��� ����)
  * maxByField (�� �����������) - ���������� ������ � ������������ ��������� ���������� ����.
  

* Query - ����� ������� � ������� �������. �����:
  
  * tableId - ������������� �������
  * query - ������ � ������� [Tables Query](https://github.com/storyclm/documentation/blob/master/TABLES_QUERY.md)
* Upsert - ������� ��� ���������� ������. ����� ��������� ������ ���� ������� ��� �������/����������. � ����������� �� ������� �������������� � �������, �� ���� ���������, ���� ���������. �����:
  
  * tableId - ������������� �������
  * storyIdName - �������� ������� �������������� ������� (������ _id)

��� ����� ������������ simple expression. ���� ��� ����� ������ ����������� ��������� (header) ���������, ��� �������� ���������� ��������� �����.
������ ������� ����� ������������ ��� ������ �������

    <to uri="storyCLM:Find?tableId=${header.getLogTableId}&amp;entityType=ru.breffi.sf_integration.dto.StoryLog&amp;maxByField=Date"/>

��� ������������� ���������� ����������� git ����������� � ���������� � ��������� maven ����������� � ������� �������  mvn install. ��� ���������� � ������ � pom ����� ������� 

     <dependency>
    	<groupId>ru.breffi</groupId>
    	<artifactId>storyclm-camel-component</artifactId>
    	<version>0.0.1-SNAPSHOT</version>
    	    <scope>compile</scope>
    </dependency>



  
