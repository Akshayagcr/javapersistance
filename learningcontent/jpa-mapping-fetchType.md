
Fetch type
    Eager   : Default for toOne relations
    Lazy    : Default for toMany relations

Best practice always mark fetch type as Lazy unless we always want some entity and then use JPQL's JOIN FETCH query to eagerly fetch the entities if required.

Relation type
    one to one
    many to one
    many to many

Table which contains foreign key column is referred to as child entity. and the referenced primary key of other table is called as parent entity

1: @OneToOne eg : post <=> postDetail

If we create a bidirectional relation then even if parent entity @OneToOne relation is mark as lazy fetch, hibernate will execute two queries as to manage any entity
it has to know its identifier. and identifier of a child entity will only be known if a query is executed.
Most efficient mapping : 
It two entities have one-to-one relation then they can share the same primary key. 
i.e.  entity which is child entity can have a single id column which acts as primary key and foreign key which references parent entity's primary key.
We use @MapsId, @OneToOne(fetch = FetchType.LAZY), and @JoinColumn (To name primary key column name in child entity or else it will be named as ParentEntity_id) annotation with reference to parent entity in child entity 
and parent entity references child entity by @OneToOne(mappedBy="childFieldName").
Still if we query find all parent entity ideally it should be a single query of SELECT *, N + 1 queries will be generated one for parent and one for child as hibernet has to decide
if child entity is present or not. To avoid this we need to set optional=false in parent entity's oneToOne clause.
We have separate table for post and postDetails instead of moving fields of postDetails in post as it enables 1: We can make postDetail optional 
2: better concurrency as if the fields were present in same table multiple concurrent transactions will not be able to update the same row, event if they are trying to update different fields

2: @ManyToOne post <=> postComments
We use this annotation on the child entity i.e. postComments entity which will have a field of Post entity.
Now to persist postComments we need to set foreignKey of post entity to do that will use 
Post p = getReferenceById(idValue); or Post p = getReference(Post.class, idValue); these method will not query DB but just return a proxy set with given id.
So there is no need to fetch Post entity by calling findById()

As we have created only unidirectional relation. we can query for postComments which will include a post entity using JOIN FETCH to load it in single SQL query

3: @OneToMany eg : post <=> postDetail
If we create a unidirectional @OneToMany relation the hibernate needs to create a third mapping table as we dont have foreign key colum stored in child entity and it is inefficient.
We can fix this by using @JoinColumn which references foreign key column in postDetail table.
event with @Join colum hibernate will execute extra SQL statements required one to insert post and then all postDetails and other update statement to update foreignKey
efficient way is to use bidirectional relation i.e. also use @ManyToOne when using @OneToMany

***** Most efficient and recommended best practice is to use only @ManyToOne relation without @OneToMany, as with @OneToMany we cannot limit the number of child entities fetch. 
it will fetch all child entities. So use only @ManyToOne annotation and when we require child entities of a parent just execute below SQL query and we can even paginate below query 
if number of child entities grow to large number
"select pc from PostComment pc where pc.post.id = :postId"
It is recommended to use List for @OneToMany annotated field instead of set, as generally if our entity will not have unique business key we will use entity identifier/primary key
and will use getClass().hashcode() will result all our entity in same bucket which is inefficient. so use list instead still in that case we need to implement equals and hashcode method
as bidirectional synchronous method will need to operate on list like list.remove(element)


4: @ManyToMany post <=> post_tag <=> tag
We need to use Set for @ManyToMany annotated field as, if we use List Hibernate generates inefficient SQL queries.


******* When implementing bidirectional relationship. add bidirectional synchronization method to parent entity which updates entity object from both side.
and when we need to persist a child entity and child entity need parent entity to set foreign key then use getByReference() method to get parent proxy instead of find() which actually fetched parent entity 



Implementing equals and HashCode for JPA entities

We need to implement equals and hashcode when we need to add entities in a set collection or want to attach entities to other persistence context
Rule : An entity must be equal to itself across all entity state i.e. transient, managed, detached. 
So we cannot use primary key as it will not be equal in transient and managed state
Best practice : use a business key which will be unique across all entity
We can mark fields which are unique like books isbn with @NaturalId it allows us to fetch entity with its natural id and we can use @NaturalIdCache to cache entity by natural id when persisting -
when we fetch the entity again by natural id it will result in zero SQL queries executed.
last option if our entity does not have a unique business or natural key is to use the primary key identifier. 
in that case in transient state id will be null as id will be assigned only when persisted to overcome this we can generate hashcode based on class i.e. use getClass().hashCode()
it will generate same constant hashcode for an entity class. and for equals method if identifier is null we check only referential equality only when id is not null we do id comparison

The toString method can use any basic entity attributes as long as the basic attributes are fetched when the entity is loaded from the database.

Cascade type:-

Cascade type are relevant when we want to propagate changes from parent entity to child entity
Cascade type : PERSIST, MERGE, REMOVE, ALL

OneToOne :
We can have Cascade type All and also remove orphan set to true.
As cascade type All inherits persist if we save parent entity it's child entity will also be saved.
If we made update to parent and child entity and did a merge operation on parent entity then it's child entity will also be updated.
If we removed parent entity then child entity will also be removed as All inherits Remove cascade type

****With orphan removal set to true. If the association between child and parent i.e. foreign key is set to null then the child entity will be deleted

OneToMany :
same as above

ManyToMany :
we cannot use cascade type remove and all as if we delete parent entity it will delete child entity as well even if it is still related to other entities. It will not alter the relationship table
So using all and remove type will have a cascading effect which will delete all child entities and then deletion of child will trigger deletion of parent entity
Therefore, using cascade type All and remove are coding mistake

To delete any one side of relationship
We need to implement custom remove method that deletes the association from the linking table


















