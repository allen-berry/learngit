CRUD  ------增删改查
Create read(select) update delete
------------------------------创建数据(插入数据)insert------------------------------
	创建数据
	insert into 表名(字段列表) values(值列表);
	-- 值要与字段列表一一对应。如果在插入的时候需要为所有的字段进行赋值。那么可以省略掉字段列表。但是，要求你的值要与表中的字段顺序，一致。
------------------------------查询数据------------------------------
	获得数据
	select 字段列表 from 表名 where 查询条件;
	--其中，字段列表可以更换成 '*',或者是 多个字段列表，用 ','隔开。查询条件可以省略。表示获取所有。where 1 //表示条件为真。未被赋值的返回的是-null。也是一个数据。占用存储空间。
------------------------------删除数据------------------------------
	delete from 表名 where 删除条件;
	--根据删除条件。来删除一些数据。同样，删除也是不可逆的操作。需要严格谨慎。语法上额可以没有where，但是逻辑上，一定要有。
------------------------------修改数据------------------------------
	update 表名 set 字段=新值 where 条件;
	--修改指定表格，满足条件的字段数据。可以同时修改多个字段，用','隔开。