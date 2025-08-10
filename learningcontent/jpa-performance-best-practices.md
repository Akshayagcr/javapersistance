
Main reasons of performance issue
1: Poor database connection management
2: Too many queries
3: Slow queries
4: Wrong JPA mapping
5: Fetching more than required

1: Poor database connection management
JDBC driver creates connections to DB and each DB connection on postgres DB is a new OS process. establishing a connection
involves TLS handshake, SSL authentication

disable open-session-in-view (spring.jpa.open-in-view=false)
disable auto commit (spring.datasource.hikari.auto-commit=false) result of it will be that a connection will be acquired when there will be first interaction with repository instead at the start of @Transactional method

do not execute a call to external service in a transactional method. if required place external service call before the first call to repository

In some cases we need to fetch data from DB and then pass it to external service in that case we can omit @Transactional and use transactionalTemplete.execute(()-> {}) 
to interact with DB

If we have @Transactional(REQUIRES_NEW) inside a method which is also annotated with @Transactional the connection opened by outer transaction need to wait for inner transaction to complete


2: Too many queries

Use getReferenceById

Service class methods should be annotated with @Transaction

Change fetch type to Lazy and use JOIN FETCH in @Query to avoid N + 1 queries problem

@DynamicUpdate to instruct hibernate to issue update query for only the fields that have changed


************ Fetch entities with intention to modify it and fetch projection for reading data

********* Hibernate and JPA are not easy instead use spring data jdbc or jooq to implement SQL queries directly !!!!!




Generate logs and statics

spring.jpa.properties.generate_statics=true
logging.level.ord.hibernate.stat=DEBUG
logging.level.ord.hibernate.SQL=DEBUG

hibernate session in spring is closed at end of a transaction

JOIN FETCH and Entity Graph to fetch related entity (More preferred JOIN FETCH)

@EntityGraph(attributePaths="nameOfAttributeToFetchEagerly")
Entity getById(Long id)



