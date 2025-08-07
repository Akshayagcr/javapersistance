Column order mater in an index with is created from multiple columns

If an index is multicolumn then we need to use all columns in where clause we cannot skip a column. As ind x is first sorted according to first column then second and third and if we skip second column we have an unsorted i.e. column in index which has no relation with other first two fields in index

If we have a inequality operator the index can only be used upto that column in an index

If we have a function that takes an index column value in where clause, then that index will not be used as database does not know what will be the outcome of a function

No single index will serve all queries. Indexing is a developer concern as developer know how their queries look like. So always check the execution plan of query using EXPLAIN in MySql and EXPLAIN ANALYZE in postgres 