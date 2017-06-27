#Kylin执行器
需要四个参数，分别为cubeName、timecycle(1、2、3、4)、buildType（‘BUILD’, ‘MERGE’, ‘REFRESH’）  
其中cubeName：Cube的名称。  
timecycle：构建周期，1表示构建昨天的数据，2表示构建前2天的数据，3表示构建前三天的数据，4表示构建前一周的数据。
buildType：构建方式。                                                                                      
参数传递方式：cubeName=fdsa&timecycle=1&buildType=BUILD