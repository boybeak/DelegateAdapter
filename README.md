#DelegateAdapter
This is an advanced RecyclerView's Adapter library. With this library, writing custom adapter class is not necessary in most conditions. Using DelegateAdapter can satisfy most of what you need.

The most amazing thing is binding multi types data and ViewHolder with Injections.

#Download
Grab via Meven:

```xml
<dependency>
  <groupId>com.github.boybeak</groupId>
  <artifactId>adapter</artifactId>
  <version>1.2.0</version>
  <type>pom</type>
</dependency>
```

or Gradle:

```groovy
compile 'com.github.boybeak:adapter:1.2.0'
```

This library requires minSdkVersion 15(Don't ask me why, I just dislike previours versions).

# Usage

I will introduce this library by 4 parts:Data, Adapter, ViewHolder and Advance Usages.

## Data

[DelegateAdapter](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateAdapter.java) only accept [LayoutImpl](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/impl/LayoutImpl.java) data. So your data model must be changed into one of the two types as below:

1. implement LayoutImpl or DelegateImpl;
2. with a delegate class extends LayoutImpl's sub classes ([AbsDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/AbsDelegate.java), [AnnotationDelegate](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/annotation/AnnotationDelegate.java)).

An example class as below:

```java
public class User {
	private int avatar;
    private String name;
    private String description;
}
```

Implement LayoutImpl:

```java
public class User {
	private int avatar;
    private String name;
    private String description;
  
	@Override
    public int getLayout() {
        return R.layout.layout_user;
    }

    @Override
    public Class<? extends AbsViewHolder> getHolderClass() {
        return UserHolder.class;
    }

    @Override
    public OnItemClickListener<LayoutImpl, AbsViewHolder> getOnItemClickListener() {
        return null;
    }

    @Override
    public OnItemLongClickListener<LayoutImpl, AbsViewHolder> getOnItemLongClickListener() {
        return null;
    }
}
```

implement DelegateImpl:

```java
public class User implements DelegateImpl<User> {

    private int avatar;
    private String name;
    private String description;
  	
	@Override
    public User getSource() {
        return this;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_user;
    }

    @Override
    public Class<? extends AbsViewHolder> getHolderClass() {
        return UserHolder.clsas;
    }

    @Override
    public OnItemClickListener<LayoutImpl, AbsViewHolder> getOnItemClickListener() {
        return null;
    }

    @Override
    public OnItemLongClickListener<LayoutImpl, AbsViewHolder> getOnItemLongClickListener() {
        return null;
    }
}
```

with a delegate class extends AbsDelegate:

```java
public class UserDelegate extends AbsDelegate<User> {
    
    private UserClickListener onClick = new UserClickListener();
    private UserLongClickListener onLongClick = new UserLongClickListener ();
    public UserDelegate(User user) {
        super(user);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_user;
    }

    @Override
    public Class<? extends AbsViewHolder> getHolderClass() {
        return UserHolder.class;
    }

    @Override
    public OnItemClickListener<LayoutImpl, AbsViewHolder> getOnItemClickListener() {
        return onClick;
    }

    @Override
    public OnItemLongClickListener<LayoutImpl, AbsViewHolder> getOnItemLongClickListener() {
        return onLongClick;
    }
}
```

with a delegate class extend AnnotationDelegate and injections:

```java
@DelegateInfo(layoutID = R.layout.layout_user, holderClass = UserHolder.class)
public class UserDelegate extends AnnotationDelegate<User> {
    @OnClick
    public Class<UserClickListener> onClick = UserClickListener.class;
  	@OnLongClick
  	public Class<UserLongClickListener> onLongClick = UserLongClickListener.class;
    public UserDelegate(User user) {
        super(user);
    }
}
```



## Adapter

The most important class is [DelegateAdapter](https://github.com/boybeak/DelegateAdapter/blob/master/adapter/src/main/java/com/nulldreams/adapter/DelegateAdapter.java). With this adapter class, there's no need to make a your own custom adapter class.

```java
DelegateAdapter adapter = new DelegateAdapter (ActivityContext);
RecyclerView rv = ...;
//setLayoutManager etc;
rv.setAdapter(adapter);
```

adapter set data like below:



## ViewHolder



## Advance Usages

