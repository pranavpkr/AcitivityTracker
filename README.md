Java Spring Boot application demonstrating CRUD application implemented with Hibernate and PostgresSQL Database.

Steps to run
1.Postgresql table created by hibernate
	CREATE TABLE emp_activities (
		id uuid DEFAULT uuid_generate_v4 () PRIMARY KEY ,
		employee_id bigint,
		activity varchar(100),
		start_time timestamp,
		duration int
	);

2. Spring boot app with two apis
	1. get request to insert data in db path = '/insert-data'
	2. to get required response from db path = '/get-data'

3. Maven program which can run by IDEs or maven command line from terminal

4. Database details stored in hibernate.cfg.xml

5. Sample input json file in required format are also attached
